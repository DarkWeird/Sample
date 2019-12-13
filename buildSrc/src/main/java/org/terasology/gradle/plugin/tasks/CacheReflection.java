package org.terasology.gradle.plugin.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.io.File;
import java.net.MalformedURLException;

public class CacheReflection extends DefaultTask {

    private FileCollection input;
    private File output;

    @InputFiles
    public FileCollection getInput() {
        return input;
    }

    public void setInput(FileCollection input) {
        this.input = input;
    }

    @OutputFile
    public File getOutput() {
        return output;
    }

    public void setOutput(File output) {
        this.output = output;
    }

    @TaskAction
    public void execute() {
        try {
            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .filterInputsBy(FilterBuilder.parsePackages("+org"))
                    .addUrls(input.getSingleFile().toURI().toURL())
                    .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner()));
            reflections.save(output.getPath());

        } catch (MalformedURLException e) {
            getLogger().error("Cannot parse input {} to url", input.getAsPath());
        }
    }
}
