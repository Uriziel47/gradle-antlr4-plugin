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
import gradle.plugin.antlr4.Antlr4Extension
/**
 *
 * @author Work
 */
class Antlr4Tool extends Tool {
    private static final Logger LOGGER = LoggerFactory.getLogger Antlr4Tool.class
   
    FileCollection grammarFiles
    
    Antlr4Extension configuration
  /*  
    	public String libDirectory;
	public boolean generate_ATN_dot = false;
	public String grammarEncoding = null; // use default locale's encoding
	public String msgFormat = "antlr";
	public boolean launch_ST_inspector = false;
	public boolean ST_inspector_wait_for_close = false;
        public boolean force_atn = false;
        public boolean log = false;
	public boolean gen_listener = true;
	public boolean gen_visitor = false;
	public boolean gen_dependencies = false;
	public String genPackage = null;
	public Map<String, String> grammarOptions = null;
	public boolean warnings_are_errors = false;
	public boolean longMessages = false;
*/
    
    Antlr4Tool(FileCollection grammarFiles) {
        super(null)
        haveOutputDir = true
        return_dont_exit = true
    }
    
    void processGrammars() {
        if(configuration != null) {
            setFromConfiguration()
        }
        
        def grammarFiles = this.grammarFiles.collect { 
            LOGGER.debug "Adding grammarFile: $it.absolutePath"
            it.absolutePath 
        }
        List<GrammarRootAST> sortedGrammars = sortGrammarByTokenVocab(grammarFiles)
        
        sortedGrammars.each { curGrammarRoot ->
            LOGGER.info "Processing grammarFile: $curGrammarRoot.fileName"
            
            def g = createGrammar(curGrammarRoot)
            g.fileName = curGrammarRoot.fileName
            LOGGER.debug "Grammar name: $g.name"
            LOGGER.debug "Grammar outdir: $outputDirectory"
            
            process(g, true)
        }
    }
    
    private void setFromConfiguration() {
         grammarEncoding = configuration.encoding
         libDirectory = configuration.lib
         msgFormat = configuration.messageFormat
         gen_listener = configuration.listener
         gen_visitor = configuration.visitor
    }
}

