package czzUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

import czz2D.CoordinateSystem;
import czz2D.Point;
import czz2D.PointSet;
import czzClusterAnalysis.Cluster;
import czzClusterAnalysis.ClusterNode;
import czzClusterAnalysis.KMeans;
import czzMatrix.Matrix;
import czzNode2Vec.Graph4N2V;
import czzNode2Vec.Node2Vec;
import czzVector.IVector;
import czzWord2Vec.Word2Vec;
import czzWord2Vec.Word2Vec.ModelType;
import czzWord2Vec.Word2Vec.TrainMethod;

/**
 * 用户界面
 * @author CZZ*/
public class UIFrame extends JFrame {

	/**
	 * 序列号*/
	private static final long serialVersionUID = 1L;
	
	/**
	 * 画布*/
	private JPanel canvas;
	
	/**
	 * 坐标系*/
	private CoordinateSystem cs;			//TODO:使用监听者模式重构操作逻辑
	
	/**
	 * 图*/
	Graph4N2V<Integer> Graph;
	
	/**
	 * Node2Vec*/
	Node2Vec n2v;
	
	/**
	 * Word2Vec*/
	Word2Vec<Integer> w2v;
	
	int kCategories;
	
	/**
	 * 聚类分析*/
	Cluster<Integer> cluster;
	
	/*================================方法 methods================================*/
	
	/**
	 * 构造方法*/
	public UIFrame(){
		super("图聚类分析");
		setExtendedState(JFrame.MAXIMIZED_BOTH);				//�?大化
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			//关闭按钮
		this.setLayout(new BorderLayout());
		initMenu();						//设置菜单栏，添加菜单�?
		initToolBar();
		initCanvas();
	}
	
	/**
	 * 设置坐标系*/
	public void setCoordinateSystem(CoordinateSystem cs) {
		this.cs = cs;
	}
	
	/**
	 * 初始化菜单*/
	@SuppressWarnings("deprecation")
	private void initMenu() {
		MenuBar menuBar = new MenuBar();
		this.setMenuBar(menuBar);
		Menu fileMenu = new Menu("文件");
		menuBar.add(fileMenu);
		MenuItem loadGraphFile = new MenuItem("加载图");
		MenuItem loadNodeSequence = new MenuItem("加载遍历序列");
		loadNodeSequence.disable();
		MenuItem loadWordVector = new MenuItem("加载词向量");
		loadWordVector.disable();
		MenuItem loadPoints = new MenuItem("加载点集合");
		loadPoints.disable();
		fileMenu.add(loadGraphFile);
		fileMenu.add(loadNodeSequence);
		fileMenu.add(loadWordVector);
		fileMenu.add(loadPoints);
		fileMenu.addSeparator();
		MenuItem exitMenu = new MenuItem("退出");
		fileMenu.add(exitMenu);
		
		ActionListener menuListener = e -> {  
            String cmd = e.getActionCommand();  
            if (cmd.equals("退出")) {  
            	int option = JOptionPane.showConfirmDialog(rootPane, "确定退出？", "退出", JOptionPane.YES_NO_OPTION); 
            	if(option == JOptionPane.YES_OPTION) System.exit(0); 
            }
            else if(cmd.equals("加载图")) {
            	/*
            	FileDialog fileLoadDialog = new FileDialog(this,"load file",FileDialog.LOAD);
            	fileLoadDialog.setVisible(true);
            	System.out.println(fileLoadDialog.getDirectory() + fileLoadDialog.getFile());
            	*/
            	JFileChooser fc = new JFileChooser();
            	fc.addChoosableFileFilter(new FileNameExtensionFilter("邻接表文件(*.edgelist)", "edgelist"));
            	int returnVal = fc.showOpenDialog(this);		//0选择1取消
            	File file = fc.getSelectedFile();
            	if(file != null && returnVal !=1) {					//选择文件
            		System.out.println(returnVal);
                	System.out.println(file.getPath() + file.getName());
            	}
            	else {
            		System.out.println(returnVal);
            	}
            }
        };
        
        fileMenu.addActionListener(menuListener);
	}
	
	/**
	 * 初始化工具栏*/
	private void initToolBar() {
		JToolBar toolBar = new JToolBar();//实例化工具条
		this.add(toolBar, BorderLayout.NORTH);
		JButton loadGraphButton = new JButton("加载图");
		JButton run = new JButton("运行");
		JTextArea graphFile = new JTextArea("");
		toolBar.add(loadGraphButton);
		toolBar.add(run);
		toolBar.add(graphFile);
		loadGraphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	JFileChooser fc = new JFileChooser();
            	fc.addChoosableFileFilter(new FileNameExtensionFilter("邻接表文件(*.edgelist)", "edgelist"));
            	int returnVal = fc.showOpenDialog(toolBar);		//0选择1取消
            	File file = fc.getSelectedFile();
            	if(file != null && returnVal !=1) {					//选择文件
            		graphFile.setText(file.getPath());
            	}
            	else {
            		//System.out.println(returnVal);
            	}
            }
        });
		
		run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String filePath = graphFile.getText();
            	if(filePath != null) {
            		Graph = new Graph4N2V<Integer>();
            		Graph.loadGraphFromEdgelistFile(filePath, " |,", false, false);
            		n2v = new Node2Vec(Graph, Node2Vec.WalkStorage.ToFile, "E:\\walks.txt");
            		n2v.setParams(1, 1, 80, 10);
            		ArrayList<Integer[]> walks = n2v.simulate_walks();
            		if(walks == null) {
            			System.out.println("遍历序列存储在文件之中");
            		}
            		//上：图的遍历； 下：通过遍历序列学习节点的向量表示
            		w2v = new Word2Vec<Integer>(Word2Vec.WordType.String, ModelType.Skip_gram, TrainMethod.HS, 5, 128, 5, 0.025f, 5, 3, 1);
            		w2v.init("E:\\walks.txt", 1);
            		w2v.startTrainning();
            		IVector[] models = w2v.getModels();
            		try {
						w2v.outputFile("karate_czz.emb");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
            		//上：word2vec； 下：k均值聚类分析
            		cluster = new KMeans<Integer>();
            		for(int i = 0; i < models.length; i++) {
            			cluster.addNode(w2v.getWordByIndex(i), models[i]);
            		}
            		kCategories = 2;							//分成3类
            		cluster.runCluster(kCategories);
            		ArrayList<ClusterNode<Integer>> clusterResult = cluster.getNodes();
            		//上：聚类分析； 下：主成分分析
            		Matrix vectors = new Matrix(models.length, w2v.getDimensions());
            		for(int i = 0; i < models.length; i++) {
            			for(int j = 0; j < w2v.getDimensions(); j++) {
            				vectors.set(i, j, models[i].getVector()[j]);			//行向量
            			}
            		}
            		Matrix pca = Matrix.PCA(vectors);				//主成分分析
            		//上：主成分分析； 下： 可视化
            		Point screenLT = new Point(-2, 2);				//（0,0）整个屏幕在第四象限
            		cs = new CoordinateSystem(canvas, screenLT, 400, 200);
            		//setCoordinateSystem(cs);
            		PointSet[] categories = new PointSet[kCategories];
            		for(int i = 0; i < kCategories; i++) {
            			categories[i] = new PointSet();
            		}
            		for(int i = 0; i < models.length; i++) {
            			categories[clusterResult.get(i).label].addPoint(new Point(pca.get(i, 0), pca.get(i, 1)));
            		}
            		for(int i = 0; i < kCategories; i++) {
            			cs.addPointSet(categories[i]);
            			categories[i].getViewPointSet().set(getRandomColor(), Color.BLACK, 2, 16);		//给每个点集涂色
            			categories[i].getViewPointSet().uniformlySet();
            		}
            		cs.draw(cs.getCanvas().getGraphics());
            	}
            }
        });
	}
	
	/**
	 * 随机一个颜色*/
	private Color getRandomColor() {
		Random rand = new Random();
		int red, green, blue;
		red = rand.nextInt(256);
		green = rand.nextInt(256);
		blue = rand.nextInt(256);
		Color color = new Color(red, green, blue);
		return color;
	}
	
	/**
	 * 初始化画布*/
	private void initCanvas() {
		canvas = new JPanel();//画布面板
		canvas.setPreferredSize(new Dimension(970, 800));
		canvas.setBackground(Color.WHITE);
		this.add(canvas, BorderLayout.CENTER);
	}
	
	/**
	 * 返回画布*/
	public Container getCanvas() {
		return canvas;
	}
}