package br.com.santander.predictbacen.reclameaqui.nlp;

import opennlp.tools.namefind.*;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.*;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class NaturalLanguageProcessingUtil {
	
	private static SentenceModel sentenceModel = null;
	
	
	private static String paragraph = "Venho através deste meio para ver se consigo resolver uma solicitação com o Banco Santander , em contato com o SAC do Santander para rever minhas taxas bancária e mudar meu plano para a cesta básica de serviço conforme resolução n 3.919 artigo 2  , solicitei o reembolso sendo assim o SAC só me liberou somente dois meses do reembolso e solicitou o contato direto com gerente do banco referente ao restante do retorno das tarifas  sendo que solicitei meus 48 meses, em contato com a gerente do Banco Santander que desconhece esta informação pediu para eu ver meus direitos com o SAC , já que consegui por este meio porque ela não poderia ajudar , sendo que eles deveriam falar a mesma língua e ser aptos a mesma informação .<br />Me senti [Editado pelo Reclame Aqui] e desrespeitada  pois não foi dada a mesma orientação tanto no SAC e com o gerente do banco .";
	
	public static void main(String args[]) throws Exception
	{
		getSentenceList();
		getTokenList();
		getPOSList();
		//train();
		getNER();
		CogrooAnalyzer.getLemmas(getSentenceList());
	}

	public static String[] getSentenceList() throws IOException 
	{
		if(sentenceModel == null)
		{
			File file = ResourceUtils.getFile("classpath:models/pt-sent.bin");
			InputStream in = new FileInputStream(file);
//			InputStream is = new NaturalLanguageProcessingUtil().getClass().getResourceAsStream("/");
			sentenceModel = new SentenceModel(in);
		}
		
		SentenceDetectorME sdetector = new SentenceDetectorME(sentenceModel);

		String sentences[] = sdetector.sentDetect(paragraph);

		for (String sentence : sentences) 
		{
			System.out.println(sentence);
		}

		return sentences;
	}
	
	public static String[] getSentenceList(String text) 
	{
		if(sentenceModel == null)
		{
			try 
			{
				File file = ResourceUtils.getFile("classpath:models/pt-sent.bin");
				InputStream in = new FileInputStream(file);
				sentenceModel = new SentenceModel(in);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		SentenceDetectorME sdetector = new SentenceDetectorME(sentenceModel);
		String sentences[] = sdetector.sentDetect(text);
		return sentences;
	}
	
	public static String[] getTokenList() throws IOException 
	{
		InputStream is = new NaturalLanguageProcessingUtil().getClass().getResourceAsStream("/models/pt-token.bin");
		TokenizerModel model = new TokenizerModel(is);
		TokenizerME tokenizer = new TokenizerME(model);
		String[] tokens = tokenizer.tokenize(paragraph);
		
		for(String token : tokens)
		{
			System.out.println(token);
		}
			    
		return tokens;
	}
	
	public static void getPOSList() throws IOException 
	{
		InputStream is = new NaturalLanguageProcessingUtil().getClass().getResourceAsStream("/models/pt-token.bin");
	    TokenizerModel model = new TokenizerModel(is);
	    TokenizerME tokenizer = new TokenizerME(model);
	    String[] tokens = tokenizer.tokenize(paragraph);
	    
	    InputStream inputStreamPOSTagger = new NaturalLanguageProcessingUtil().getClass().getResourceAsStream("/models/pt-pos-perceptron.bin");
	    	POSModel posModel = new POSModel(inputStreamPOSTagger);
	    	POSTaggerME posTagger = new POSTaggerME(posModel);
	    	String tags[] = posTagger.tag(tokens);
	    	
	    	System.out.println( "TOKEN == TAG? "+ (tags.length == tokens.length) );
	    	
	    	for(int i = 0; i < tokens.length; i++)
		{
	    		System.out.println(tokens[i] + " - " + tags[i]);
		}
	}

	public static void train() throws Exception
	{
		ObjectStream<String> lineStream =
				new PlainTextByLineStream(new MarkableFileInputStreamFactory(new File("/Users/cristianogomes/Downloads/apache-opennlp-1.9.1/bin/corpus.txt")), StandardCharsets.UTF_8);

		TokenNameFinderModel model;

		ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);
		
		
		String modelFile = "/Users/cristianogomes/Downloads/apache-opennlp-1.9.1/bin/pt-ner.bin";
		
		try 
		{
			model = NameFinderME.train("pt", "person", sampleStream, TrainingParameters.defaultParams(), new TokenNameFinderFactory());
			
			BufferedOutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
		 	model.serialize(modelOut);
		}
		catch(Exception e) {e.printStackTrace();}
	}
	
	
	public static void getNER() throws Exception
	{
		InputStream inputStreamNameFinder = new NaturalLanguageProcessingUtil().getClass().getResourceAsStream("/models/pt-ner.bin");
		TokenNameFinderModel model = new TokenNameFinderModel(inputStreamNameFinder);
		
		NameFinderME nameFinder = new NameFinderME(model);
		
		String tokens[] = getTokenList();
		Span nameSpans[] = nameFinder.find(tokens);
		
		for(Span s: nameSpans) 
		{
			System.out.println(s.toString() + "  " + tokens[s.getStart()]);
		}
	}
	
	
	
	public static String[] getTokenArray(String text) throws IOException 
	{
		InputStream is = new NaturalLanguageProcessingUtil().getClass().getResourceAsStream("/models/pt-token.bin");
		TokenizerModel model = new TokenizerModel(is);
		TokenizerME tokenizer = new TokenizerME(model);
		String[] tokens = tokenizer.tokenize(text);
		
		for(String token : tokens)
		{
			System.out.println(token);
		}
			    
		return tokens;
	}
	
	
	public static List<String> getNERList( String tokens[] ) throws IOException 
	{
		List<String> nerList = new ArrayList<String>();
		
		InputStream inputStreamNameFinder = new NaturalLanguageProcessingUtil().getClass().getResourceAsStream("/models/pt-ner.bin");
		TokenNameFinderModel model = new TokenNameFinderModel(inputStreamNameFinder);
		
		NameFinderME nameFinder = new NameFinderME(model);
		
		Span nameSpans[] = nameFinder.find(tokens);
		
		for(Span s: nameSpans) 
		{
			System.out.println(s.toString() + "  " + tokens[s.getStart()]);
		}
		return nerList;
	}
}
