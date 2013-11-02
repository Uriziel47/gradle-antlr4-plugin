/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gradle.plugin.antlr4

import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.InputDirectory
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
    
    @InputDirectory
    File inputDirectory
    
    @TaskAction
    void generate() {
        Antlr4Extension cfg = project.antlr4
        
        def antlr = new Antlr4Tool()
        antlr.configuration = cfg
        
        LOGGER.info "Input directory: $inputDirectory"
        
        source.each { curSource ->
            LOGGER.info "Source: $curSource"
            def packagePath = getPackagePath(curSource)
            def packageName = getPackageName(packagePath)
            
            LOGGER.info "Package path: $packagePath"
            LOGGER.info "Package name: $packageName"
            
            antlr.outputDirectory = "${outputDirectory.absolutePath}\\$packagePath"
            antlr.genPackage = packageName == '' ? null : packageName
            antlr.grammarFiles = project.files(project.relativePath(curSource))
            antlr.processGrammars()
        }
        
        
        
        LOGGER.info "Hello from ANTLR4 Project: $project.name}"
        LOGGER.info "Antlr4.encoding: $cfg.encoding"
        LOGGER.info "Antlr4.lib: $cfg.lib"
        LOGGER.info "Antlr4.messageFormat: $cfg.messageFormat"
        LOGGER.info "Antlr4.listener: $cfg.listener"
        LOGGER.info "Antlr4.visitor: $cfg.visitor"
        LOGGER.info "Antlr4.outputDir: $outputDirectory.absolutePath"
    }
    	
    String getPackagePath(File source) {
        def packagePath = source.absolutePath - inputDirectory.absolutePath
        packagePath = packagePath[1..-1]
        packagePath = packagePath.replaceAll(/(.*)\\.+\.(g4|g)/, '$1')
        packagePath
    }
    
    String getPackageName(String packagePath) {
        def packageName = packagePath.replaceAll('\\\\', '.')
        packageName.trim()
    }
}


