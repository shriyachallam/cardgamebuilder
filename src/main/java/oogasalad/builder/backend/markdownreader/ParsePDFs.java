package oogasalad.builder.backend.markdownreader;

import com.dansoftware.pdfdisplayer.PDFDisplayer;
import javafx.scene.Parent;

import java.io.IOException;
import java.io.InputStream;

public class ParsePDFs {
    private PDFDisplayer pdfRender;
    private final String DOCS_PATH = "BuilderUI/BuilderHelpFiles/UserDocs.pdf";
    private final String CONFIG_PATH = "BuilderUI/BuilderHelpFiles/GameConfigGuide.pdf";
    private final String SETUP_VIEW = "oogasalad.builder.frontend.views.SetUpBuilder";

    public ParsePDFs(String view) throws IOException {
        System.out.println("Current view: " + view);
        pdfRender = new PDFDisplayer();
        pdfRender.setSecondaryToolbarToggleVisibility(false);
        String [] elements = new String[]{"sidebarToggle", "findNext", "zoomIn", "zoomOut", "print", "openFile", "download"};
        disableUI(elements);

        String filePath = (view.equals(SETUP_VIEW)) ? CONFIG_PATH : DOCS_PATH;

        //String path = "BuilderUI/BuilderHelpFiles/" + view.substring(view.lastIndexOf('.') + 1) + "_Help.pdf";

        System.out.println("Looking for help doc: " + filePath);
        InputStream pdfStream = getClass().getClassLoader().getResourceAsStream(filePath);
        pdfRender.loadPDF(pdfStream); // OR PDFDisplayer(InputStream)
    }

    private void disableUI(String[] elements) {
        for (String e: elements) {
            pdfRender.setVisibilityOf(e, false);
        }
    }

    public Parent getRenderedPDF() {
        return pdfRender.toNode();
    }
}
