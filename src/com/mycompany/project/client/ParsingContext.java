package com.mycompany.project.client;

public class ParsingContext {

    public int ip;
    private String[] source;
    private int line;
    
    public ParsingContext(String source) {
        this.source = source.split("\n");
    }
    
    public String nextLine() throws Exception {
        if(line > source.length) {
            throw new Exception("Reached end of source code");
        }
        return source[line++];
    }
    
    public boolean hasNext() {
        return line < source.length;
    }
}
