package br.com.santander.predictbacen.reclameaqui.model;


import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Created by agibsonccc on 10/9/14.
 *
 * Neural net that processes text into wordvectors. See below url for an in-depth explanation.
 * https://deeplearning4j.org/word2vec.html
 */
public class Word2VecRawTextExample {

    private static Logger log = LoggerFactory.getLogger(Word2VecRawTextExample.class);

    public static void main2(String[] args) throws Exception {

        // Gets Path to Text file
        String filePath = "/Users/cristianogomes/Downloads/reclameaqui_description_v1.xlsx";

        System.out.println("Load & Vectorize Sentences....");
        // Strip white space before and after for each line
        SentenceIterator iter = new BasicLineIterator(filePath);
        // Split on white spaces in the line to get words
        TokenizerFactory t = new DefaultTokenizerFactory();

        /*
            CommonPreprocessor will apply the following regex to each token: [\d\.:,"'\(\)\[\]|/?!;]+
            So, effectively all numbers, punctuation symbols and some special symbols are stripped off.
            Additionally it forces lower case for all tokens.
         */
        t.setTokenPreProcessor(new CommonPreprocessor());

        System.out.println("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .iterations(1)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();

        System.out.println("Fitting Word2Vec model....");
        vec.fit();

        System.out.println("Writing word vectors to text file....");

        
        WordVectorSerializer.writeWord2VecModel(vec, "/Users/cristianogomes/Downloads/word2vec_min5.txt");
        
        
        
        
        Collection<String> lst;
		try {
			// Prints out the closest 10 words to "day". An example on what to do with these Word Vectors.
			System.out.println("Closest Words:");
			lst = vec.wordsNearestSum("app", 3);
			System.out.println("3 Words closest to 'app':"+ lst.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        try {
			Word2Vec w2v = WordVectorSerializer.readWord2VecModel("/Users/cristianogomes/Downloads/word2vec_min5.txt");
			lst = w2v.wordsNearestSum("tarifa", 3);
			System.out.println("3 Words closest to 'tarifa': "+ lst.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // TODO resolve missing UiServer
//        UiServer server = UiServer.getInstance();
//        System.out.println("Started on port " + server.getPort());
    }
    
    
    public static void main(String[] args) throws Exception {

    		String word = "fatura";
    	
        Collection<String> lst;
		
        try {
			Word2Vec w2v = WordVectorSerializer.readWord2VecModel("/Users/cristianogomes/Downloads/word2vec.txt");
			lst = w2v.wordsNearestSum(word, 10);
			
			System.out.println(word + " index is " + w2v.indexOf(word) );
			System.out.println(word + " is in vocab? " + w2v.hasWord(word) );
			
			System.out.println("20 Words closest to " + word + ": " + lst.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // TODO resolve missing UiServer
//        UiServer server = UiServer.getInstance();
//        System.out.println("Started on port " + server.getPort());
    }
}
