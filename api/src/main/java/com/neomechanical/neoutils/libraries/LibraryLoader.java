// CHECKSTYLE:OFF
package com.neomechanical.neoutils.libraries;

import com.neomechanical.neoutils.NeoUtils;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.repository.RepositoryPolicy;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.resolution.DependencyResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transfer.AbstractTransferListener;
import org.eclipse.aether.transfer.TransferEvent;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class LibraryLoader {
    private final RepositorySystem repository;
    private final DefaultRepositorySystemSession session;
    private final List<RemoteRepository> repositories;

    public LibraryLoader(@NotNull RemoteRepository... repositories) {

        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);

        this.repository = locator.getService(RepositorySystem.class);
        this.session = MavenRepositorySystemUtils.newSession();

        session.setChecksumPolicy(RepositoryPolicy.CHECKSUM_POLICY_FAIL);
        session.setLocalRepositoryManager(repository.newLocalRepositoryManager(session, new LocalRepository("libraries")));
        session.setTransferListener(new AbstractTransferListener() {
            @Override
            public void transferStarted(@NotNull TransferEvent event) {
                NeoUtils.getNeoUtilities().getFancyLogger().info("Downloading " + event.getResource().getRepositoryUrl() + event.getResource().getResourceName());
            }
        });
        session.setReadOnly();

        this.repositories = Arrays.asList(repositories);
    }

    @Nullable
    public ClassLoader createLoader(@NotNull JavaPlugin plugin) {
        PluginDescriptionFile desc = plugin.getDescription();
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "plugin.yml"));
        List<String> libraries = config.getStringList("libraries");

        if (libraries.isEmpty()) {
            return null;
        }
        NeoUtils.getNeoUtilities().getFancyLogger().info("[" + desc.getName() + "] " + "Loading " + libraries.size() + " libraries... please wait");

        List<Dependency> dependencies = new ArrayList<>();
        for (String library : libraries) {
            Artifact artifact = new DefaultArtifact(library);
            Dependency dependency = new Dependency(artifact, null);

            dependencies.add(dependency);
        }

        DependencyResult result;
        try {
            result = repository.resolveDependencies(session, new DependencyRequest(new CollectRequest((Dependency) null, dependencies, repositories), null));
        } catch (DependencyResolutionException ex) {
            throw new RuntimeException("Error resolving libraries", ex);
        }

        List<URL> jarFiles = new ArrayList<>();
        for (ArtifactResult artifact : result.getArtifactResults()) {
            File file = artifact.getArtifact().getFile();

            URL url;
            try {
                url = file.toURI().toURL();
            } catch (MalformedURLException ex) {
                throw new AssertionError(ex);
            }

            jarFiles.add(url);

            NeoUtils.getNeoUtilities().getFancyLogger().info("[" + desc.getName() + "] " + "Loaded library " + file);
        }

        return new URLClassLoader(jarFiles.toArray(new URL[0]), getClass().getClassLoader());
    }
}