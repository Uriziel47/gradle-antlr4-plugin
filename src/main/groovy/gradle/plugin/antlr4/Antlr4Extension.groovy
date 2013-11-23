/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gradle.plugin.antlr4

/**
 *
 * @author alexander.sedlmayr
 */
class Antlr4Extension {
    String encoding = 'utf-8'
    String lib = null
    String messageFormat = 'antlr'
    boolean listener = true
    boolean visitor = false

    def encoding (String value)  {
        this.encoding = value
    }

    def lib(String value) {
        this.lib = value
    }

    def messageFormat(String value) {
        this.messageFormat = value
    }

    def listener(boolean value) {
        this.listener = value
    }

    def visitor(boolean value) {
        this.visitor = value
    }
}

