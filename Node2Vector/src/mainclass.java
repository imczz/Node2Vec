import czzGraph.Graph;

public class mainclass {

	public static void main(String[] args) {
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
	}
}
