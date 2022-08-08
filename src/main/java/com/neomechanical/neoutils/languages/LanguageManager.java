package com.neomechanical.neoutils.languages;

import com.neomechanical.neoutils.config.ConfigUpdater;
import com.neomechanical.neoutils.messages.Logger;
import com.neomechanical.neoutils.utils.UtilManager;
import org.apache.commons.io.IOUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.io.*;
import java.util.*;
import java.util.function.Function;

import static com.neomechanical.neoutils.NeoUtils.setLanguageManager;

public class LanguageManager {
    final Map<String, String> internalPlaceholderReplacements;
    private final JavaPlugin main;
    File languageFolder = null;
    private File languageConfigFile = null;
    private FileConfiguration languageConfig;
    private static String currentLanguage = "en";
    private static String languageCode = "en-US";

    private FileConfiguration defaultLanguageConfig = null;
    private Map<String, Function<Player, String>> internalPlaceholders;
    private static final ArrayList<String> languageFiles = new ArrayList<>();


    /**
     * Create a new LanguageManager for the given plugin.
     *
     * @param main the main class
     * @param files the language files to load, ensure you add the .yml extension
     */
    public LanguageManager(final JavaPlugin main, String... files) {
        this.main = main;
        internalPlaceholderReplacements = new HashMap<>();
        languageFiles.addAll(Arrays.asList(files));
        setLanguageManager(this);
    }

    public static void setLanguage(String languageCode) {
        LanguageManager.languageCode = languageCode;
    }
    public static void addLanguage(String languageCode) {
        LanguageManager.languageFiles.add(languageCode);
    }
    private void loadMissingDefaultLanguageFiles() {
        //Create the Language Data Folder if it does not exist yet (the NotQuests/languages folder)
        languageFolder = new File(main.getDataFolder().getPath() + "/languages/");


        if (!languageFolder.exists()) {
            if (!languageFolder.mkdirs()) {
                Logger.warn("There was an error creating the languages folder.");
                return;
            }
        }

        for (final String fileName : languageFiles) {
            try {
                File file = new File(languageFolder, fileName);

                if (!file.exists()) {
                    if (!file.createNewFile()) {
                        Logger.warn("There was an error creating the " + fileName + " language file. (3)");
                        return;
                    }

                    InputStream inputStream = main.getResource("translations/" + fileName);
                    //Instead of creating a new language file, we will copy the one from inside the plugin jar into the plugin folder:
                    if (inputStream != null) {
                        try (OutputStream outputStream = new FileOutputStream(file)) {
                            IOUtils.copy(inputStream, outputStream);
                        } catch (Exception e) {
                            Logger.warn("There was an error creating the " + fileName + " language file. (4)");
                            return;
                        }

                    }
                }


                //Doesn't matter if the en-US.yml exists in the plugin folder or not, because we're reading it from the internal resources folder
                if (fileName.equals("en-US.yml")) {
                    //Copy to default.yml
                    File defaultFile = new File(languageFolder, "default.yml");

                    InputStream inputStream = main.getResource("translations/en-US.yml");
                    //Instead of creating a new language file, we will copy the one from inside the plugin jar into the plugin folder:
                    if (inputStream != null) {
                        try (OutputStream defaultOutputStream = new FileOutputStream(defaultFile)) {
                            IOUtils.copy(inputStream, defaultOutputStream);
                            //Put into fileConfiguration

                            if (!defaultFile.exists()) {
                                Logger.warn("There was an error reading the default.yml language file. (5)");
                                return;
                            }
                            defaultLanguageConfig = new YamlConfiguration();
                            defaultLanguageConfig.load(defaultFile);

                        } catch (Exception e) {
                            Logger.warn("There was an error creating the default.yml language file. (6)");
                            return;
                        }
                    } else {
                        Logger.warn("There was an error creating the default.yml language file. (7)");
                        return;
                    }


                }


            } catch (IOException ioException) {
                Logger.warn("There was an error creating the " + fileName + " language file. (3)");
                return;
            }
        }


    }


    /**
     * Load language configs
     */
    public final void loadLanguageConfig() {
        if (languageCode==null) {
            throw new IllegalStateException("Language code is not set");
        }
        loadMissingDefaultLanguageFiles();

        /*
         * If the generalConfigFile Object doesn't exist yet, this will load the file
         * or create a new general.yml file if it does not exist yet and load it into the
         * generalConfig FileConfiguration object.
         */
        if (languageConfigFile == null || !currentLanguage.equals(languageCode)) {

            //Create the Data Folder if it does not exist yet (the NotQuests folder)

            if (languageFolder == null) {
                languageFolder = new File(main.getDataFolder().getPath() + "/languages/");
            }

            if (!languageFolder.exists()) {
                if (!languageFolder.mkdirs()) {
                    Logger.warn("There was an error creating the NeoPerformance languages folder.");
                    return;
                }

            }

            languageConfigFile = new File(languageFolder, languageCode + ".yml");
            try {
                if (!languageConfigFile.exists()) {
                    Logger.warn("There was an error getting the " + languageCode + " language file. (1)");
                    return;
                }
                ConfigUpdater.update(main, "translations/" + languageCode + ".yml", languageConfigFile, List.of(""));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!languageConfigFile.exists()) {
                //Does not work yet, since comments are overridden if something is saved
                //saveDefaultConfig();
                try {
                    //Try to create the language.yml config file, and throw an error if it fails.

                    if (!languageConfigFile.createNewFile()) {
                        Logger.warn("There was an error creating the " + languageCode + ".yml language file.");
                        return;

                    }
                } catch (IOException ioException) {
                    Logger.warn("There was an error creating the " + languageCode + ".yml language file.");
                    return;
                }
            }
            languageConfig = new YamlConfiguration();
            try {
                languageConfig.load(languageConfigFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        } else {
            languageConfig = YamlConfiguration.loadConfiguration(languageConfigFile);
        }


        if (setupDefaultStrings()) {
            saveLanguageConfig();
        }
        currentLanguage = languageCode;

    }

    private boolean setupDefaultStrings() {
        //Set default values

        if (defaultLanguageConfig == null) {
            Logger.warn("There was an error reading the default.yml language configuration.");
            return false;
        }

        boolean valueChanged = false;
        final ConfigurationSection defaultConfigurationSection = defaultLanguageConfig.getConfigurationSection("");
        if (defaultConfigurationSection != null) {
            for (final String defaultString : defaultConfigurationSection.getKeys(true)) {

                if (!defaultConfigurationSection.isString(defaultString)) {
                    continue;
                }

                if (!getLanguageConfig().isString(defaultString)) {
                    getLanguageConfig().set(defaultString, defaultConfigurationSection.getString(defaultString));
                    valueChanged = true;
                }
            }
        }


        return valueChanged;

    }


    /**
     * This will return the language FileConfiguration object
     */
    public final FileConfiguration getLanguageConfig() {
        if (languageConfig == null) {
            loadLanguageConfig();
        }
        return languageConfig;
    }

    public final String getString(final String languageString, @Nullable Player player) {
        if (!getLanguageConfig().isString(languageString)) {
            return "Language string not found: " + languageString;
        } else {
            final String translatedString = getLanguageConfig().getString(languageString);
            if (translatedString == null) {
                return "Language string not found: " + languageString;
            }
            return applySpecial(ChatColor.translateAlternateColorCodes('&', applyInternalPlaceholders(translatedString, player)));
        }
    }
    public LanguageManager setInternalPlaceholders(Map<String, Function<Player, String>> placeholders) {
        this.internalPlaceholders = placeholders;
        return this;
    }
    private String applyInternalPlaceholders(String initialMessage, @Nullable Player player) {
        internalPlaceholderReplacements.clear();
        for (Map.Entry<String, Function<Player, String>> entry : internalPlaceholders.entrySet()) {
            internalPlaceholderReplacements.put(entry.getKey(), entry.getValue().apply(player));
        }
        return UtilManager.replaceFromMap(initialMessage, internalPlaceholderReplacements);
    }

    private String applySpecial(String initialMessage) {
        initialMessage = initialMessage.replace("<EMPTY>", " ");


        final StringBuilder finalMessage = new StringBuilder();

        final String[] splitMessages = initialMessage.split("\n");
        for (int index = 0; index < splitMessages.length; index++) {
            finalMessage.append(splitMessages[index]);
            if (index < splitMessages.length - 1) {
                finalMessage.append("\n");
            }
        }


        return finalMessage.toString();
    }


    /**
     * This will try to save the language configuration file with the data which is currently in the
     * languageConfig FileConfiguration object.
     */
    public void saveLanguageConfig() {
        try {
            getLanguageConfig().save(languageConfigFile);

        } catch (IOException ioException) {
            ioException.printStackTrace();
            Logger.severe("Language Config file could not be saved.");
        }
    }

}
