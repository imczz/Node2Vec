//import czzGraph.Graph;
import java.util.ArrayList;

import czzNode2Vec.Node2Vec;
import czzVector.IVector;
import czzWord2Vec.Word2Vec;
import czzWord2Vec.Word2Vec.ModelType;
import czzWord2Vec.Word2Vec.TrainMethod;

public class mainclass {

	public static void main(String[] args) {
		/*
		Graph G = new Graph(false, true);
		int i;

		for(i = 0; i < 6; i++) {
			G.addNode(i);
		}
		for(i = 0; i < 5; i++) {
			G.addEdge(i, i + 1, 9);
			G.addEdge(i + 1, i, 8);
		}
		G.addEdge(0, 3, 7);
		G.addEdge(1, 4, 6);
		G.addEdge(2, 5, 5);
		
		System.out.println(G.toMatrixString());
		System.out.println("Hello world!");
		System.out.println(G.removeNode(3));
		System.out.println(G.toMatrixString());
		System.out.println("Hello world!");
		G.addNode(6);
		G.addNode(3);
		System.out.println(G.toMatrixString());
		System.out.println("Hello world!");
		G.addEdge(3, 3, 6);
		G.addEdge(6, 6, 7);
		System.out.println(G.toMatrixString());
		System.out.println("Hello world!");
		G.removeEdge(2, 5);
		System.out.println(G.toMatrixString());
		System.out.println("Hello world!");
		System.out.println(G.toMatrixString());
		System.out.println(G.loadGraphFromEdgeListFile("C:\\Users\\CZZ\\Desktop\\N2V\\graph\\karate.edgelist", false, false));
		System.out.println("Hello world!");
		G.clear();
		//System.out.println(G.loadGraphFromEdgeListFile("C:\\Users\\CZZ\\Desktop\\N2V\\graph\\karate.edgelist", false, false));
		//System.out.println(G.toMatrixString());
		*/
		Graph4N2V<Integer> G = new Graph4N2V<Integer>();
		G.loadGraphFromFile("C:\\Users\\CZZ\\Desktop\\N2V\\graph\\karate.edgelist", "edgelist", false, false);
		Node2Vec n2v = new Node2Vec(G);
		n2v.setParams(1, 1, 80, 10);
		ArrayList<Integer[]> walks = n2v.simulate_walks();
		//System.out.println(walks.size());
		Word2Vec<Integer> w2v = new Word2Vec<Integer>(ModelType.Skip_gram, TrainMethod.HS, 0, 128, 5, 0.025f, 5, 3, 1);
		w2v.init(walks);
		w2v.startTrainning();
		IVector[] models = w2v.getModels();
		System.out.println(models);
	}
}
