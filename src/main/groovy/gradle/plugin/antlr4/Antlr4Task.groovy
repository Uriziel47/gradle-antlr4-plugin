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
    
    //@InputFiles
    FileCollection antlrClasspath
    
    //@OutputDirectory
    File outputDirectory
    
    //@InputDirectory
    File inputDirectory
    
    @TaskAction
    void generate() {
        Antlr4Extension cfg = project.antlr4
        
        def antlr = new Antlr4Tool()
        antlr.configuration = cfg
        
        LOGGER.debug "Input directory: $inputDirectory"
        def packageMap = [:]
        
        source.each { curSource ->
            delegate = project
            LOGGER.debug "Source: $curSource"
            def packagePath = getPackagePath(curSource)
            def packageName = getPackageName(packagePath)
            
            LOGGER.debug "Package path: $packagePath"
            LOGGER.debug "Package name: $packageName"
            
            if(packageMap.containsKey(packageName)) {
                packageMap[packageName].source << relativePath(curSource)
            } else {
                packageMap.put(
                    packageName, 
                    [pName: packageName, pPath: packagePath, source: files(relativePath(curSource))]
                )
            }
        }
        
        LOGGER.debug "PackageMap size: ${packageMap.size()}"
        
        packageMap.values().each { curValue ->
            LOGGER.debug "PackageMap value: $curValue"
            
            antlr.outputDirectory = "${outputDirectory.absolutePath}\\$curValue.pPath"
            File outputDir = new File(antlr.outputDirectory)
            def created = outputDir.mkdirs()
            antlr.genPackage = curValue.pName == '' ? null : curValue.pName
            antlr.grammarFiles = curValue.source
            
            LOGGER.debug "Created directory $antlr.outputDirectory - $created"
            antlr.processGrammars()
        }
        
        LOGGER.debug "Hello from ANTLR4 Project: $project.name}"
        LOGGER.debug "Antlr4.encoding: $cfg.encoding"
        LOGGER.debug "Antlr4.lib: $cfg.lib"
        LOGGER.debug "Antlr4.messageFormat: $cfg.messageFormat"
        LOGGER.debug "Antlr4.listener: $cfg.listener"
        LOGGER.debug "Antlr4.visitor: $cfg.visitor"
        LOGGER.debug "Antlr4.outputDir: $outputDirectory.absolutePath"
    }
    	
    String getPackagePath(File source) {
        LOGGER.debug "Source: $source.absolutePath"
        LOGGER.debug "InputDir: $inputDirectory.absolutePath"
        def packagePath = source.absolutePath.capitalize() - inputDirectory.absolutePath.capitalize()
        packagePath = packagePath[1..-1]
        packagePath = packagePath.replaceAll(/(.*)\\.+\.(g4|g)/, '$1')
        
        LOGGER.debug "PackageDir: $packagePath"
        packagePath
    }
    
    String getPackageName(String packagePath) {
        def packageName = packagePath.replaceAll('\\\\', '.')
        packageName.trim()
    }
}


