package es.jcyl.barquejo.app.licitacyl.parser;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by josecarlos.delbarrio on 20/09/2016.
 */
public abstract class GenericParser<M> implements ContentHandler {

    // ATRIBUTOS
    private static final String NODE_ELEMENT = "element";
    private static final String NODE_ATTRIBUTE = "attribute";
    private static final String NODE_STRING = "string";

    private static final String ATTR_NAME = "name";

    private static final int CONTEXT_NOCONTEXT = 0;

    protected int xmlContext = CONTEXT_NOCONTEXT;

    private List<M> dtos = new java.util.LinkedList<M>();
    private M currDTO = null;
    private StringBuilder strBuilder = new StringBuilder();
    private boolean isUnderAppendableElement = false;
    private String appendableUnder = null;

    // MÉTODOS ABSTRACTOS
    public abstract String getAppendableOnlyUnderElement(int context);
    public abstract void setAttributeValue(int context, String value, M dto);
    public abstract M createNewDTO();
    public abstract int getContextForAttribute(String attr);


    //CONSTRUCTOR
    public GenericParser() {

    }


    public Collection<M> parse(InputStream is) throws ParserException {

        SAXParserFactory factory= SAXParserFactory.newInstance();

        try {
            SAXParser sp= factory.newSAXParser();
            XMLReader reader=sp.getXMLReader();
            reader.setContentHandler(this);
            InputSource iSrc = new InputSource(is);
            iSrc.setEncoding("utf-8");
            reader.parse(iSrc);

        } catch (SAXException saxpe) {
            System.err.println("Catched SAXException");
            saxpe.printStackTrace();
            throw new ParserException("SAXException found");

        } catch (ParserConfigurationException pce) {
            System.err.println("Catched ParserConfigurationException");
            pce.printStackTrace();
            throw new ParserException("ParserConfigurationException found");

        } catch (IOException ioe) {
            System.err.println("Catched IOException");
            ioe.printStackTrace();
            throw new ParserException("IOException found");
        }
        Collection<M> lst = this.dtos;
        this.dtos = null;
        this.currDTO = null;
        this.xmlContext = CONTEXT_NOCONTEXT;
        return lst;
    }

    @Override
    public void characters(char ch[], int start, int length)
            throws SAXException {
        String string = new String(ch, start, length);
        if (this.xmlContext != CONTEXT_NOCONTEXT && (isUnderAppendableElement || getAppendableOnlyUnderElement(this.xmlContext) == null)) {
            // Only append to string builder if we are under string node or there is no need of a string node
            strBuilder.append(string);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        if (localName.equals(NODE_ELEMENT)) {
            this.dtos.add(this.currDTO);
            this.currDTO = null;
            this.xmlContext = CONTEXT_NOCONTEXT;

        } else if (NODE_ATTRIBUTE.equals(localName) && appendableUnder == null) {
            setAttributeValue(this.xmlContext, strBuilder.toString().replaceAll("\\n", ""), this.currDTO);
            this.xmlContext = CONTEXT_NOCONTEXT;

        } else if (localName.equals(appendableUnder)) { // End of string (in case of array addd value
            setAttributeValue(this.xmlContext, strBuilder.toString(), this.currDTO);
            strBuilder = new StringBuilder();
            isUnderAppendableElement = false;
        }

    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    public void ignorableWhitespace(char ch[], int start, int length)
            throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    public void processingInstruction(String target, String data)
            throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDocumentLocator(Locator locator) {
        // TODO Auto-generated method stub

    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    public void startDocument() throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes atts) throws SAXException {
        if (NODE_ELEMENT.equals(localName)) {
            // Entering a new element
            this.currDTO = createNewDTO();
        } else if (NODE_ATTRIBUTE.equals(localName)) {
            // New attribute, find out which one
            String name = atts.getValue(ATTR_NAME);

            this.xmlContext = CONTEXT_NOCONTEXT;

            this.xmlContext = getContextForAttribute(name);
            strBuilder = new StringBuilder();
            this.appendableUnder = getAppendableOnlyUnderElement(this.xmlContext);

        } else if (localName.equals(this.appendableUnder)) {
            isUnderAppendableElement = true;
        }
    }

    @Override
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        // TODO Auto-generated method stub

    }
}
