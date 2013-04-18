import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;

import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.util.PDFTextStripper;


public class PdfParser {

   /**
   * @param args
   */
   // TODO 自动生成方法存根

       public   static   void   main(String[]   args)   throws   Exception{ 
            FileInputStream   fis   =   new   FileInputStream("D:\\TestPdfParser.pdf"); 
            BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\TestPdfParser.txt"));
            PDFParser   p   =   new   PDFParser(fis); 
            p.parse();         
            PDFTextStripper   ts   =   new   PDFTextStripper();         
            String   s   =   ts.getText(p.getPDDocument()); 
            writer.write(s);
            System.out.println(s); 
            fis.close(); 
            writer.close();
          
   }
}