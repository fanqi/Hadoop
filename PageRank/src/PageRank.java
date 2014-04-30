public class PageRank {

	public static void main(String[] args) {
		PageRank pageRank = new PageRank();
		// 原始矩阵sMatrix
		double[][] sMatrix = { { 0, 0.5, 0.5, 0, 0.5 }, { 0.25, 0, 0, 0, 0 },
				{ 0.25, 0, 0, 1, 0.5 }, { 0.25, 0.5, 0.5, 0, 0 },
				{ 0.25, 0, 0, 0, 0 } };
		// 全1矩阵uMatrix
		double[][] uMatrix = { { 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1 },
				{ 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1 } };
		// 节点数量n
		int n = 5;
		// 权重值 alpha ，取值0-1之间，常用的是0.85，Google会根据经验调整
		double alpha = 0.85;
		// 计算gMatrix
		System.out.println("gMatrix：");
		double[][] gMatrix = pageRank.getGMatrix(sMatrix, uMatrix, n, alpha);
		// PR向量
		//初始PR向量向量
		double[] qInit = { 1, 1, 1, 1, 1 };
		//当前PR向量，第一个PR向量
		double[] qCur = qInit;
		//下一个PR向量
		double[] qNext = qInit;
		//向量距离
		double distance;
		//向量距离阀值
		double thresholdDistance=0.000001;
		//迭代次数
		int iterations=0;
		do {
			iterations++;
			qCur = qNext;
			qNext = pageRank.getNextVector(gMatrix, qCur);
			distance = pageRank.getDistance(qNext, qCur);
			System.out.println("\nPageRankVector（迭代"+iterations+"次）：");
			for (double d : qNext) {
				System.out.print(d + "\t");
			}
		} while (distance > thresholdDistance);
		
	}

	private double[] getNextVector(double[][] matrix, double[] vector) {
		double[] returnVector = new double[vector.length];
		for (int i = 0; i < returnVector.length; i++) {
			for (int j = 0; j < returnVector.length; j++) {
				returnVector[i] += matrix[i][j] * vector[j];
			}
		}
		return returnVector;
	}

	private double getDistance(double[] fromVector, double[] toVector) {
		double distance = 0.0d;
		for (int i = 0; i < fromVector.length; i++) {
			distance += Math.pow((toVector[i] - fromVector[i]), 2);
		}
		distance = Math.sqrt(distance);
		return distance;
	}

	private double[][] getGMatrix(double[][] sMatrix, double[][] uMatrix,
			int n, double alpha) {
		double[][] gMatrix = new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				gMatrix[i][j] = alpha * sMatrix[i][j] + (1 - alpha) / n
						* uMatrix[i][j];
				System.out.print(gMatrix[i][j] + "	");
			}
			System.out.println();
		}

		return gMatrix;
	}
}
