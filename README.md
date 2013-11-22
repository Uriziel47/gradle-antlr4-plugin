gradle-antlr4-plugin
====================

This is a gradle plugin for developing ANTLR4

Usage
-----
In build.gradle:

    apply plugin: 'antlr4'

Project layout
--------------

    src/main/antlr4/{packagepath}         Production ANTLR4 grammar files.
    src/test/antlr4/{packagepath}         Test ANTLR4 grammar files.
    src/{sourceset}/antlr4/{packagepath}  ANTLR4 grammar files for the given source set

Tasks
-----
Related to the ANTLR Plugin from gradle equivalent tasks will be created for each sourceset:

    generateGrammarSource   
    generateTestGrammarSource   
    generate{SourceSet}GrammarSource    
    
The tasks will create the generated grammar files into    
    
    src/main/antlr4/{packagepath}         ----> src-gen/main/antlr4/{packagepath}
    src/test/antlr4/{packagepath}         ----> src-gen/test/antlr4/{packagepath}
    src/{sourceset}/antlr4/{packagepath}  ----> src-gen/{sourceset}/antlr4/{packagepath}
    
Configuration
-------------

A gradle extension is added for configure the ANTLR4 generation (defaults are shown here)    

    antlr4 {
      encoding = 'utf-8'
      lib = null
      messageFormat = 'antlr'
      listener = true
      visitor = false
    }


Miscellaneous
-------------

- No task dependencies are added
