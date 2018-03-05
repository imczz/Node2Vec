# Node2Vec JAVA实现
参考http://snap.stanford.edu/node2vec/。
在图的遍历时，通过p和q两个参数，平衡深度优先（DFS）和广度优先（BFS）。
git：https://github.com/snap-stanford/snap/tree/master/examples/node2vec

预计实现三个部分：
1. 存储图的结构czzGraph
  1.1 图的存储，图的节点，图的边
  1.2 图的输入（邻接表，邻接矩阵）
  1.3 图的可视化（优先级较低的工作）
2. 主体：czzNode2Vec，图的节点产生遍历序列，为转化为向量表示做准备
  2.1 The Alias Method: Efficient Sampling with Many Discrete Outcomes，
      https://hips.seas.harvard.edu/blog/2013/03/03/the-alias-method-efficient-sampling-with-many-discrete-outcomes/
      官网git的python代码中，提供了这个随机方法的实现，我准备使用JAVA重新实现，探讨他的作用，并且与自己的方法比较性能
  2.2 Node2Vec
3. czzWord2Vec
  官方git中，main.py中有一句model = Word2Vec(walks, size=args.dimensions, window=args.window_size, min_count=0, sg=1, workers=args.workers, iter=args.iter)
  前面的czzNode2Vec模块只是将图中的节点转化为遍历序列，把得到的遍历序列看成是一个个句子，之后就可以把“句子”中的“单词（节点）”通过Word2Vec中的skip-gram方法转化为向量。
