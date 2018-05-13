import java.io.IOException;
import java.util.ArrayList;

import czzNode2Vec.Node2Vec;
import czzVector.IVector;
import czzWord2Vec.Word2Vec;
import czzWord2Vec.Word2Vec.ModelType;
import czzWord2Vec.Word2Vec.TrainMethod;

public class mainclass {

	public static void main(String[] args) {
		Graph4N2V<Integer> G = new Graph4N2V<Integer>();
		G.loadGraphFromFile("karate.edgelist", "edgelist", false, false);
		Node2Vec n2v = new Node2Vec(G);
		n2v.setParams(1, 1, 80, 10);
		ArrayList<Integer[]> walks = n2v.simulate_walks();
		//System.out.println(walks.size());
		//Word2Vec<Integer> w2v = new Word2Vec<Integer>(ModelType.Skip_gram, TrainMethod.HS, 0, 128, 5, 0.025f, 5, 5, 1);
		Word2Vec<Integer> w2v = new Word2Vec<Integer>(ModelType.Skip_gram, TrainMethod.NS, 5, 128, 5, 0.05f, 5, 5, 1);
		//Word2Vec<Integer> w2v = new Word2Vec<Integer>(ModelType.Skip_gram, TrainMethod.BOTH, 5, 128, 5, 0.025f, 5, 10, 1);
		w2v.init(walks);
		w2v.startTrainning();
		IVector[] models = w2v.getModels();
		System.out.println(models.length);
		try {
			w2v.outputFile("karate_czz.emb");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
