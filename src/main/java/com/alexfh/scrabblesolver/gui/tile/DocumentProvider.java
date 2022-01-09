package com.alexfh.scrabblesolver.gui.tile;

import com.alexfh.scrabblesolver.state.IScrabbleGameState;
import com.alexfh.scrabblesolver.util.ScrabbleUtil;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DocumentProvider {

    public static DocumentProvider INSTANCE = new DocumentProvider();

    private final SAXSVGDocumentFactory documentFactory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
    private final Document[][][][] tileDocuments = new Document[IScrabbleGameState.alphaChars.length][2][2][2];
    private final Document[] wildcardDocuments = new Document[2];
    private final Document[][] blankTileDocuments = new Document[2][2];
    private Document defaultBlankDocument;

    public void init() throws IOException {
        this.initTileDocuments();
        this.initWildcardDocuments();
        this.initBlankTileDocuments();
        this.initDefaultBlankDocument();
    }

    private void initTileDocuments() throws IOException {
        for (int c = 0; c < IScrabbleGameState.alphaChars.length; c++) {
            char letter = IScrabbleGameState.alphaChars[c];

            for (int w = 0; w < 2; w++) {
                boolean isWild = w != 0;

                for (int i = 0; i < 2; i++) {
                    boolean isIso = i != 0;

                    for (int h = 0; h < 2; h++) {
                        boolean isHighlighted = h != 0;

                        String fileName = isIso ? "iso" : "flat";
                        fileName += isHighlighted ? "high" : "norm";
                        fileName += Character.toUpperCase(letter);
                        fileName += isWild ? "w" : "";
                        Document newDocument = this.getDocumentFromFile(new File("src/main/resources/assets/tile/" + fileName + ".svg"));
                        this.tileDocuments[c][w][i][h] = newDocument;
                    }
                }
            }
        }
    }

    private void initWildcardDocuments() throws IOException {
        this.wildcardDocuments[0] = this.getDocumentFromFile(new File("src/main/resources/assets/tile/flatwild.svg"));
        this.wildcardDocuments[1] = this.getDocumentFromFile(new File("src/main/resources/assets/tile/isowild.svg"));
    }

    private void initBlankTileDocuments() throws IOException {
        this.blankTileDocuments[0][0] = this.getDocumentFromFile(new File("src/main/resources/assets/tile/blankdl.svg"));
        this.blankTileDocuments[0][1] = this.getDocumentFromFile(new File("src/main/resources/assets/tile/blanktl.svg"));
        this.blankTileDocuments[1][0] = this.getDocumentFromFile(new File("src/main/resources/assets/tile/blankdw.svg"));
        this.blankTileDocuments[1][1] = this.getDocumentFromFile(new File("src/main/resources/assets/tile/blanktw.svg"));
    }

    private void initDefaultBlankDocument() throws IOException {
        this.defaultBlankDocument = this.getDocumentFromFile(new File("src/main/resources/assets/tile/blank.svg"));
    }

    private Document getDocumentFromFile(File sfgFile) throws IOException {
        FileReader reader = new FileReader(sfgFile);
        Document document = this.documentFactory.createDocument(null, new FileReader(sfgFile));

        reader.close();

        return document;
    }

    public Document getTileDocument(char letter, boolean isWild, boolean isIso, boolean isHighlighted) {
        return this.tileDocuments[ScrabbleUtil.charToInt(letter)][isWild ? 1 : 0][isIso ? 1 : 0][isHighlighted ? 1 : 0];
    }

    public Document getWildcardDocument(boolean isIso) {
        return this.wildcardDocuments[isIso ? 1 : 0];
    }

    public Document getBlankTileDocument(int letterMultiplier, int wordMultiplier) {
        if (letterMultiplier == 1 && wordMultiplier == 1) return this.getDefaultBlankDocument();

        if (letterMultiplier > 1 && letterMultiplier < 4) {
            return this.blankTileDocuments[0][letterMultiplier - 2];
        } else if (wordMultiplier > 1 && wordMultiplier < 4) {
            return this.blankTileDocuments[1][wordMultiplier - 2];
        } else {
            return null;
        }
    }

    public Document getDefaultBlankDocument() {
        return this.defaultBlankDocument;
    }

}
