# Node2Vec JAVA实现
参考http://snap.stanford.edu/node2vec/ 。在图的遍历时，通过p和q两个参数，平衡深度优先（DFS）和广度优先（BFS）。

git：https://github.com/snap-stanford/snap/tree/master/examples/node2vec

## 1. 存储图的结构czzGraph
- 1.1 图的存储，图的节点，图的边
- 1.2 图的输入（邻接表，邻接矩阵）
- 1.3 图的可视化（优先级较低的工作）

## 2. 主体：czzNode2Vec，图的节点产生遍历序列，为转化为向量表示做准备
- 2.1 The Alias Method: Efficient Sampling with Many Discrete Outcomes，
      https://hips.seas.harvard.edu/blog/2013/03/03/the-alias-method-efficient-sampling-with-many-discrete-outcomes/
      官网git的python代码中，提供了这个随机方法的实现，我准备使用JAVA重新实现，探讨他的作用，并且与“轮盘赌法”随机比较性能
- 2.2 Node2Vec
## 3. czzWord2Vec
  官方代码（上面第3行git）中，main.py中有一句`model = Word2Vec(walks, size=args.dimensions, window=args.window_size, min_count=0, sg=1, workers=args.workers, iter=args.iter)`
  
  前面的czzNode2Vec模块只是将图中的节点转化为遍历序列，把得到的遍历序列看成是一个个句子，之后就可以把“句子”中的“单词”（节点）通过Word2Vec中的`skip-gram`方法转化为向量。
  
# K均值聚类分析
聚类分析Cluster类是一个抽象类，其中存储了ClusterNode聚类节点，K均值聚类KMeans继承Cluster类，实现了runCluster(int k)方法。其实，聚类分析不只有划分方法，还有层次方法等等，也不一定需要指定分类数k，但是当前先实现一种简单的，未来通过重构，可以增加更多的聚类方式。

# 简易矩阵计算器
为了便于展示，使用**主成分分析法**（PCA）来降维。需要计算向量们A（A的每一行代表一个节点向量，A的每一列代表向量的一个分量）的协方差矩阵cov（方差越大，波动越大，信息量也就越大），协方差矩阵是一个(A的列数)阶方阵，计算cov的特征值与特征向量（特征值代表了矩阵的能量分布），选取特征值绝对值最大的两个特征值对应的特征向量，组成（(A的列数) × 2）的矩阵P，B=A×P，矩阵B就是降维之后的向量们，B的每一行是节点向量，B的每一列是新的向量分量。

尝试手动写一个矩阵计算器，最重要的功能是求（实对称）矩阵的（最大2个）特征值。

线性代数中学过，通过行列式|λE - A| = 0来解方程，求特征值λ，但是不适用于计算机计算。通过数值分析，计算机一般采用QR分解法，A=QR，Q是一个正交矩阵，R是一个上三角矩阵，QR分解可以用Gram-Schmidt正交化，或者householder旋转等方法，Q^TAQ的特征值，与A的特征值相同，Q^TAQ=Q^TQRQ=RQ，也就是A=QR，A1=RQ，A1=Q1R1，A2=R1Q1……，通过迭代，R对角线上的元素就会越来越接近特征值矩阵D。

1.施密特正交化方法QR分解只适用于方阵，而且要求矩阵各个列向量线性无关（矩阵满秩），而householder旋转原理稍微复杂一些，我不想再挖新的坑。2.我们只需要最大的两个特征向量，不需要全部的特征向量，否则还要排序（实际通过matlab的[D,V]=eig(cov)查看，测试数据34个节点，128维向量，特征值126个数量级均在1e-15到1e-19之间）。基于一二两点，我们选择**幂法**求方阵特征值与特征向量。

幂法只能求**绝对值最大**的特征值，但是**数值分析**提供了两种**收缩方法**，其中一种是先求出主特征值λ1，对应特征向量x1，可以构造一个向量v，使x1^T*v=1（向量的数量积），C=A-λ1*x1*v^T，C就是A去掉主特征值影响的矩阵，再对C使用幂法，可以求出C矩阵绝对值最大的特征值λ2，还有对应的特征向量y2，那么原矩阵A第二大特征值对应的特征向量x2=(λ2-λ1)*y2 + λ1*(v' * y2)*x1。

注：v的构造方式，v=1/λ1/x1i*(ai1, ai2, ……, ain)'	其中λ1为绝对值最大特征值,x1i为l1对应特征向量x1中非0分量，i为这个分量的下标，也是矩阵A的行；也就是取得A的第i行，转置之后，除（λ1 * x1i）i∈[0, n-1]。

# 简易2D显示引擎
可视化引擎用MVC方式设计，model控制逻辑模型，view控制显示模型，controller完成控制功能。在当前程序中，m是一个无限大的逻辑坐标系，v是屏幕坐标系，控制器完成选择拖动缩放等功能。想象一下，把屏幕（屏幕坐标系）的一个方框，左上角screenLT放在逻辑坐标系上，通过kx与ky（这样就可以使x轴与y轴有不同的单位长度）参数，完成逻辑坐标系与屏幕坐标系中的点的联系（双向映射计算）。

未来通过重构，坐标系CoordinateSystem类也会有自己的ViewCoordinateSystem显示坐标系类，其中装载了现在写在CoordinateSystem类中的screenLT、kx、ky、ViewPoint等，为什么要这样写呢，想象一下在第一人称射击（FPS）游戏中，地图有一张，玩家却有多个，每个玩家都显示不同的内容，这些玩家显示的内容只是地图不同部分通过中心投影计算出来的。诚然，2D引擎可以简单一点，只有一个观察者，一个坐标系，1个model，多个view的设计有过度设计的嫌疑。

拖动与缩放功能很容易实现，只需要更改View的screenLT属性，或者kx、ky就可以实现。但是如果用户想通过点击选择某个点，进一步查看这个点来自哪个图中的节点，该如何设计呢，最简单的，可以把鼠标点击的点通过screenLT与k，逆运算回逻辑坐标系，遍历点的ArrayList，查找最近的点，鼠标点击也会有一个响应区域（比如半径3像素的圆），在相应区域内最近的点捕获鼠标（也可以认为是鼠标捕获了最近的点），通过点的id（这也是为什么要增加一个GeometricElement类的原因），反向查找其余信息。

MVC模式的好处就是便于拓展，如果想做一个2D（甚至3D）的游戏，只需要设计：线Line类（Line可能是一个父类，直线射线线段分别继承Line；线还可能有多种构造方式，比如垂线、角平分线、中垂线等，不单单是两个点就能描述的），圆Circle类，多边形polygon类。与model对应，就会有各种view，比如ViewLine、ViewCircle等，view存储了自己在哪个ViewCoordinateSystem中，根据ViewCoordinateSystem记录的“逻辑屏幕映射”参数计算自身的屏幕坐标，还有颜色，线型（虚线、点线），填充色等。
