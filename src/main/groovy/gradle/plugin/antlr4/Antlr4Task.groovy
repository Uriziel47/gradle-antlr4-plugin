/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gradle.plugin.antlr4

import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.io.File
import java.util.List

import org.antlr.v4.Tool
import gradle.plugin.antlr4.internal.Antlr4Tool

/**
 *
 * @author Work
 */
class Antlr4Task extends SourceTask {
    
    private static final Logger LOGGER = LoggerFactory.getLogger Antlr4Task.class
    
    @InputFiles
    FileCollection antlrClasspath
    
    @OutputDirectory
    File outputDirectory
    
    @TaskAction
    void generate() {
        def antlr = new Antlr4Tool()
        antlr.grammarFiles = source
        
        antlr.processGrammars()
    }
    	
}


