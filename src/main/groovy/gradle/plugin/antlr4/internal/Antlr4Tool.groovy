/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gradle.plugin.antlr4.internal

import org.antlr.v4.Tool
import org.gradle.api.file.FileCollection
import org.antlr.v4.tool.ast.GrammarRootAST
import org.slf4j.Logger
import org.slf4j.LoggerFactory
/**
 *
 * @author Work
 */
class Antlr4Tool extends Tool {
    private static final Logger LOGGER = LoggerFactory.getLogger Antlr4Tool.class
   
    FileCollection grammarFiles
    
    Antlr4Tool(FileCollection grammarFiles) {
        super(null)
    }
    
    void processGrammars() {
        def grammarFiles = this.grammarFiles.collect { 
            LOGGER.info "Adding grammarFile: $it.absolutePath"
            it.absolutePath 
        }
        List<GrammarRootAST> sortedGrammars = sortGrammarByTokenVocab(grammarFiles)
        
        sortedGrammars.each { curGrammarRoot ->
            LOGGER.info "Processing grammarFile: $curGrammarRoot.fileName"
            
            def g = createGrammar(curGrammarRoot)
            g.fileName = curGrammarRoot.fileName
            
            process(g, true)
        }
    }
    
}

