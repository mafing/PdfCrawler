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
        //是否排序
        boolean sort = false;
        //pdf文件名
        String pdfFile = file;
        //输入文本文件名称
        String textFile = null;
        //编码方式
        String encoding = "UTF-8";
        //开始提取页数
        int startPage = 1;
        //结束提取页数
        int endPage = Integer.MAX_VALUE;
        //文件输入流，输入文本文件
        Writer output = null; 
        //内存中存储的PDF Document
        PDDocument document = null;
        
        try{
            try{
                //首先当作一个URL来加载文件，如果得到异常再从本地系统装载文件
                URL url = new URL(pdfFile);
                document = PDDocument.load(url);
                String fileName = url.getFile();
            
                if(fileName.length() > 4){
                    //以原来pdf名称来命名新产生的txt文件
                    File outputFile = new File(fileName.substring(0, fileName.length()-4) + ".txt");
                    textFile = outputFile.getName();
                }            
            }catch(Exception e){
                //如果作为URL装载得到异常则从文件系统装载
                document = PDDocument.load(pdfFile);
                if(pdfFile.length() > 4){
                    textFile = pdfFile.substring(0, pdfFile.length() - 4) + ".txt";
                }
            }
            //文件输出流，写入文件到textFile
            output = new OutputStreamWriter(new FileOutputStream(textFile),encoding);
            //PDFTextStripper来提取文本
            PDFTextStripper stripper = new PDFTextStripper();
            //设置是否排序
            stripper.setSortByPosition(sort);
            //设置起始页
            stripper.setStartPage(startPage);
            //设置结束页
            stripper.setEndPage(endPage);
            //调用PDFTextStripper的writeText提取并输出文本
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
        String pdfpath = "D:\\index\\Lucene.Net基本用法.pdf";
        
        IndexWriter writer = new IndexWriter(path, new StandardAnalyzer(),true);
        //writer.setMaxFieldLength(10240);
        //LucenePDFDocument返回由PDF产生的Lucene Document
        Document d = LucenePDFDocument.getDocument(new File(pdfpath));
        //System.out.println(d);
        //写入索引
        writer.addDocument(d);
        writer.close();
        
        //读取d:\index下的索引文件，建立IndexSearcher
        IndexSearcher searcher = new IndexSearcher(path);
        //对索引的contents Field进行关键字Query的查找
        Term t = new Term("contents","优");
        Term m = new Term("contents","化");
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
            System.out.println("文档编号：" + doc[i].doc);
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
            //test.getText("D:\\index\\Lucene.Net基本用法.pdf");
            test.LuceneTest();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}