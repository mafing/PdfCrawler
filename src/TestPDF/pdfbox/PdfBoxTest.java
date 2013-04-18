package TestPDF.pdfbox;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocCollector;
import org.apache.lucene.search.TopDocs;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.searchengine.lucene.LucenePDFDocument;
import org.pdfbox.util.PDFTextStripper;

public class PdfBoxTest {

    public void getText(String file) throws Exception{
        //�Ƿ�����
        boolean sort = false;
        //pdf�ļ���
        String pdfFile = file;
        //�����ı��ļ�����
        String textFile = null;
        //���뷽ʽ
        String encoding = "UTF-8";
        //��ʼ��ȡҳ��
        int startPage = 1;
        //������ȡҳ��
        int endPage = Integer.MAX_VALUE;
        //�ļ��������������ı��ļ�
        Writer output = null; 
        //�ڴ��д洢��PDF Document
        PDDocument document = null;
        
        try{
            try{
                //���ȵ���һ��URL�������ļ�������õ��쳣�ٴӱ���ϵͳװ���ļ�
                URL url = new URL(pdfFile);
                document = PDDocument.load(url);
                String fileName = url.getFile();
            
                if(fileName.length() > 4){
                    //��ԭ��pdf�����������²�����txt�ļ�
                    File outputFile = new File(fileName.substring(0, fileName.length()-4) + ".txt");
                    textFile = outputFile.getName();
                }            
            }catch(Exception e){
                //�����ΪURLװ�صõ��쳣����ļ�ϵͳװ��
                document = PDDocument.load(pdfFile);
                if(pdfFile.length() > 4){
                    textFile = pdfFile.substring(0, pdfFile.length() - 4) + ".txt";
                }
            }
            //�ļ��������д���ļ���textFile
            output = new OutputStreamWriter(new FileOutputStream(textFile),encoding);
            //PDFTextStripper����ȡ�ı�
            PDFTextStripper stripper = new PDFTextStripper();
            //�����Ƿ�����
            stripper.setSortByPosition(sort);
            //������ʼҳ
            stripper.setStartPage(startPage);
            //���ý���ҳ
            stripper.setEndPage(endPage);
            //����PDFTextStripper��writeText��ȡ������ı�
            stripper.writeText(document, output);
        }finally{
            if(output != null){
                output.close();                
            }
            if(document != null){
                document.close();
            }
        }        
    }
    
    /** *//**
     * test Lucene with pdfbox
     * @throws IOException
     */
    public void LuceneTest() throws IOException{
        
        String path = "D:\\index";
        String pdfpath = "D:\\index\\Lucene.Net�����÷�.pdf";
        
        IndexWriter writer = new IndexWriter(path, new StandardAnalyzer(),true);
        //writer.setMaxFieldLength(10240);
        //LucenePDFDocument������PDF������Lucene Document
        Document d = LucenePDFDocument.getDocument(new File(pdfpath));
        //System.out.println(d);
        //д������
        writer.addDocument(d);
        writer.close();
        
        //��ȡd:\index�µ������ļ�������IndexSearcher
        IndexSearcher searcher = new IndexSearcher(path);
        //��������contents Field���йؼ���Query�Ĳ���
        Term t = new Term("contents","��");
        Term m = new Term("contents","��");
        PhraseQuery q = new PhraseQuery();
        q.add(t);
        q.add(m);
        //Query q = new TermQuery(t);
        TopDocCollector co = new TopDocCollector(10);
        searcher.search(q,co);
        
        Document document;
        TopDocs docs = co.topDocs();
        ScoreDoc[] doc = docs.scoreDocs;
        //System.out.println(doc.length);
        
        for(int i=0;i<doc.length;i++){
            System.out.println("�ĵ���ţ�" + doc[i].doc);
            //document = searcher.doc(doc[i].doc);
        }
    }
    /** *//**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        PdfBoxTest test = new PdfBoxTest();
        try{
            //test.getText("D:\\index\\Lucene.Net�����÷�.pdf");
            test.LuceneTest();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}