package br.com.santander.predictbacen.reclameaqui.nlp;

import org.cogroo.analyzer.Analyzer;
import org.cogroo.analyzer.ComponentFactory;
import org.cogroo.text.Document;
import org.cogroo.text.Sentence;
import org.cogroo.text.Token;
import org.cogroo.text.impl.DocumentImpl;
import org.cogroo.text.impl.SentenceImpl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;

public class CogrooAnalyzer 
{
	private static Analyzer cogroo = null;
	
	public static Analyzer getAnalyzer()
	{
		if( cogroo == null )
		{
			ComponentFactory factory = ComponentFactory.create(CogrooAnalyzer.class.getResourceAsStream("/models_onair_pt_BR.xml"));
			cogroo = factory.createPipe();
		}
		return cogroo;
	}



	/**
	 * A utility method that prints the analyzed document to a writer
	 * 
	 * @throws IOException
	 */
	private static void print(Document document) throws IOException 
	{
		// and now we navigate the document to print its data
		for (Sentence sentence : document.getSentences()) 
		{
			for (Token token : sentence.getTokens()) 
			{
				// F == null ? other_F : F

				String lexeme = token.getLexeme();

				String[] lemmaArr = token.getLemmas();
				String lemmas;
				if (lemmaArr == null || token.getLemmas().length == 0) 
				{
					lemmas = "-";
				} 
				else 
				{
					lemmas = Arrays.toString(token.getLemmas());
				}
				String pos = token.getPOSTag();
				String feat = token.getFeatures();
				String chunk = token.getChunkTag();
				
				
				System.out.println("pos:"+pos);
				System.out.println("feat:"+feat);
				System.out.println("chunk:"+chunk);
				System.out.println("lemmas:"+lexeme.toString());
				System.out.println("lexeme:"+lexeme);
				

				if (feat == null)
					feat = "-";

				// check if it is a MWE
				boolean isMWE = false;
				if (lexeme.contains("_")) 
				{
					String coverred = sentence.getText().substring(token.getStart(), token.getEnd());
					if (coverred.contains(" ")) 
					{
						isMWE = true;

						// we handle it here...
						String[] parts = coverred.split("\\s+");
						String tag = "B-" + pos;

						for (String tok : parts) 
						{
							String out = String.format("%-15s %-12s %-6s %-10s %-6s\n", tok, "[" + tok + "]", tag, feat,
									chunk);
							System.out.print(out);

							tag = "I-" + pos;
							chunk = chunk.replace("B-", "I-");
						}
					}
				}

				if (!isMWE) 
				{
					String out = String.format("%-15s %-12s %-6s %-10s %-6s\n", lexeme, lemmas, pos, feat, chunk);
					System.out.print(out);
				}
			}

			System.out.print("\n");

		}

	}

	public static void getLemmas(String[] sentences) 
	{
		Charset charset = null;
		try 
		{
			charset = Charset.forName("UTF-8");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			System.out.println("Invalid charset encoding: " + charset);
		}

		// looks nice! now we can start cogroo with the language

		Analyzer cogrooAnalyzer = CogrooAnalyzer.getAnalyzer();

		// and open the input/output files

		try 
		{
			for (String sentence : sentences) 
			{
				// text.append(scanner.nextLine() + NL);
				Document document = new DocumentImpl();
				document.setText(sentence);

				Sentence sent = new SentenceImpl(0, sentence.length(), document);
				document.setSentences(Collections.singletonList(sent));

				cogrooAnalyzer.analyze(document);
				print(document);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static boolean hasLemma(String[] sentences, String lemmaType, String lemmaValue) 
	{
		boolean flagContains = false;
		
		Charset charset = null;
		try 
		{
			charset = Charset.forName("UTF-8");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			System.out.println("Invalid charset encoding: " + charset);
		}

		// looks nice! now we can start cogroo with the language

		Analyzer cogrooAnalyzer = CogrooAnalyzer.getAnalyzer();

		// and open the input/output files

		try 
		{
			for (String sentence : sentences) 
			{
				// text.append(scanner.nextLine() + NL);
				Document document = new DocumentImpl();
				document.setText(sentence);

				Sentence sent = new SentenceImpl(0, sentence.length(), document);
				document.setSentences(Collections.singletonList(sent));

				cogrooAnalyzer.analyze(document);
				flagContains = contains(document, lemmaType, lemmaValue);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return flagContains;
	}
	
	private static boolean contains(Document document, String lemmaType, String lemmaValue) throws IOException 
	{
		boolean contains = false;

		for (Sentence sentence : document.getSentences()) 
		{
			for (Token token : sentence.getTokens()) 
			{
				String lexeme = token.getLexeme();

				String[] lemmaArr = token.getLemmas();

				if( lemmaArr != null && lemmaArr.length > 0 ) 
				{
					for(String lemma : lemmaArr)
					{
						if( lemma.equalsIgnoreCase(lemmaValue) )
						{
							contains = true;
							break;
						}
					}
				}
			}
		}
		
		return contains;
	}
}