package com.neomechanical.neoutils.commands.annotations;

import com.neomechanical.neoutils.commands.Command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SubCommands {
    Class<? extends Command>[] subcommands();
}
