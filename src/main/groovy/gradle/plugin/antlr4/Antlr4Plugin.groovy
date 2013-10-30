/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gradle.plugin.antlr4

import org.gradle.api.Plugin
import org.gradle.api.Project

import org.gradle.api.Action;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.plugins.DslObject;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.internal.tasks.DefaultSourceSet;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.plugins.antlr.internal.AntlrSourceVirtualDirectoryImpl;
import org.gradle.api.tasks.SourceSet;

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Inject;
import java.io.File;
import java.util.concurrent.Callable;

import static org.gradle.api.plugins.JavaPlugin.COMPILE_CONFIGURATION_NAME;
import org.gradle.api.internal.file.DefaultSourceDirectorySet

/**
 *
 * @author Work
 */
class Antlr4Plugin implements Plugin<Project>{
    private static final Logger LOGGER = LoggerFactory.getLogger Antlr4Plugin.class
    
    public static final String ANTLR_CONFIGURATION_NAME = "antlr4";
    private final FileResolver fileResolver;
    
    @Inject
    public Antlr4Plugin(FileResolver fileResolver) {
        this.fileResolver = fileResolver;
    }
    
    void apply(Project project) {
        
        project.plugins.apply JavaPlugin.class

        // set up a configuration named 'antlr' for the user to specify the antlr libs to use in case
        // they want a specific version etc.
        Configuration antlrConfiguration = project.configurations.create(ANTLR_CONFIGURATION_NAME).setVisible(false)
                .setTransitive(false).setDescription("The Antlr libraries to be used for this project.")
        project.configurations.getByName(COMPILE_CONFIGURATION_NAME).extendsFrom(antlrConfiguration)

        project.convention.getPlugin(JavaPluginConvention.class).sourceSets.all(
                new Action<SourceSet>() {
                    public void execute(SourceSet sourceSet) {
                        final String srcDir = "src/${sourceSet.name}/antlr4"
                        final String displayName = "${sourceSet.displayName} Antlr4 source"
                        DefaultSourceDirectorySet antlr4SourceSet = new DefaultSourceDirectorySet(displayName, fileResolver)
                        antlr4SourceSet.srcDirs(srcDir)
                        antlr4SourceSet.filter.include '**/*.g4'
                        antlr4SourceSet.filter.include '**/*.g'
                        sourceSet.allSource.source(antlr4SourceSet)

                        LOGGER.info "Creating new SourceSet: $srcDir, ${fileResolver == null}"
                        
                        antlr4SourceSet.getSrcDirs().each { curSrcDir ->
                            LOGGER.info "SrcDir: $curSrcDir"
                        }
                    
                    
                        final String taskName = sourceSet.getTaskName('generate', 'GrammarSource')
                        Antlr4Task antlrTask = project.tasks.create(taskName, Antlr4Task.class)
                        antlrTask.description = "Processes the $sourceSet.name Antlr grammars."

                        antlrTask.source = antlr4SourceSet

                        // 4) set up convention mapping for handling the 'antlr' dependency configuration
                        antlrTask.getConventionMapping().map("antlrClasspath", new Callable<Object>() {
                            public Object call() throws Exception {
                                return project.getConfigurations().getByName(Antlr4Plugin.ANTLR_CONFIGURATION_NAME).copy()
                                        .setTransitive(true);
                            }
                        });

                        // 5) Set up the Antlr output directory (adding to javac inputs!)
                        final String outputDirectoryName = "${project.buildDir}/src-gen/${sourceSet.name}/antlr4"
                        final File outputDirectory = new File(outputDirectoryName)
                        antlrTask.outputDirectory = outputDirectory
                        sourceSet.getJava().srcDir(outputDirectory)

                        // 6) register fact that antlr should be run before compiling
                        //project.getTasks().getByName(sourceSet.getCompileJavaTaskName()).dependsOn(taskName)
                    }
                });
        
            project.extensions.create('antlr4', Antlr4Extension.class)
    }
}

