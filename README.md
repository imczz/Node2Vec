# Node2Vec JAVA
参考http://snap.stanford.edu/node2vec/，在图的遍历时，通过p和q两个参数，平衡深度优先（DFS）和广度优先（BFS）。
git：https://github.com/snap-stanford/snap/tree/master/examples/node2vec

预计实现三个部分：
1. 存储图的结构czzGraph
  1.1 图的存储，图的节点，图的边
  1.2 图的输入（邻接表，邻接矩阵）
  1.3 图的可视化（优先级较低的工作）
2. 主体：czzNode2Vec，图转化为向量表示
  2.1 The Alias Method: Efficient Sampling with Many Discrete Outcomes，
      https://hips.seas.harvard.edu/blog/2013/03/03/the-alias-method-efficient-sampling-with-many-discrete-outcomes/
      官网git的python代码中，提供了这个随机方法的实现，我准备使用JAVA重新实现，探讨他的作用，并且与自己的方法比较性能
  2.2 Node2Vec
3. Word2Vec
  官方git中，main.py中有一句model = Word2Vec(walks, size=args.dimensions, window=args.window_size, min_count=0, sg=1, workers=args.workers, iter=args.iter)
  需要探讨他与Node2Vec的关系，并且考虑实现
