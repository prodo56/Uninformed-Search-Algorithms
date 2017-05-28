package homework1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

public class homework {
	public static class Node {
		private static AtomicInteger numGenerator = new AtomicInteger();
		private int cost, trafficdata, trafficFreedata, totalCost, order,
				secorder;
		private Boolean visited, explored;

		public Boolean getExplored() {
			return explored;
		}

		public void setExplored(Boolean explored) {
			this.explored = explored;
		}

		public Boolean getVisited() {
			return visited;
		}

		public void setVisited(Boolean visited) {
			this.visited = visited;
		}

		public Boolean isVisited() {
			return visited;
		}

		public int getCost() {
			return cost;
		}

		public void setCost(int cost) {
			this.cost = cost;
		}

		public String getParent() {
			return parent;
		}

		public void setParent(String parent) {
			this.parent = parent;
		}

		public int getTrafficdata() {
			return trafficdata;
		}

		public void setTrafficdata(int trafficdata) {
			this.trafficdata = trafficdata;
		}

		public int getTrafficFreedata() {
			return trafficFreedata;
		}

		public void setTrafficFreedata(int trafficFreedata) {
			this.trafficFreedata = trafficFreedata;
		}

		public String getNode() {
			return node;
		}

		public void setNode(String node) {
			this.node = node;
		}

		private String node, parent;

		Node(String name, int data) {
			this.node = name;
			this.trafficdata = data;
			this.visited = Boolean.FALSE;
			this.explored = Boolean.FALSE;
			// this.order=numGenerator.getAndIncrement();
		}

		Node(String name) {
			this.node = name;
			this.visited = Boolean.FALSE;
			this.explored = Boolean.FALSE;
			this.order = numGenerator.getAndIncrement();
		}

		public Boolean isExplored() {
			return explored;
		}

		public int getOrder() {
			return order;
		}

		public void setOrder(int order) {
			this.order = order;
		}

		public int getTotalCost() {
			return totalCost;
		}

		public void setTotalCost(int totalCost) {
			this.totalCost = totalCost;
		}

		public int getSecorder() {
			return secorder;
		}

		public void setSecorder(int secorder) {
			this.secorder = secorder;
		}

	}

	public static void bfs(Map<String, List<Node>> graphList,
			Map<String, Node> nodes, String start, String goal) {
		System.out.println("BFS");
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(new Node(start));
		nodes.get(start).setVisited(Boolean.TRUE);
		Node n = null;
		while (!queue.isEmpty()) {
			n = queue.poll();

			if (nodes.get(goal).isVisited()) {
				// System.out.println("Goal Found");
				// System.out.println("Cost:" + nodes.get(goal).getCost());
				backtrace(nodes, start, goal);
				return;
			}
			List<Node> list = graphList.get(n.getNode());
			if (list != null) {
				for (Node m : list) {
					if (!nodes.get(m.getNode()).isVisited()) {
						queue.add(m);
						nodes.get(m.getNode()).setParent(n.getNode());
						nodes.get(m.getNode()).setCost(
								nodes.get(n.getNode()).getCost() + 1);
						nodes.get(m.getNode()).setVisited(Boolean.TRUE);
					}
				}
			}
		}
		// System.out.println("No goal Found");
	}

	public static void dfs(Map<String, List<Node>> graphList,
			Map<String, Node> nodes, String start, String goal) {
		System.out.println("DFS");
		Stack<Node> stack = new Stack<Node>();
		stack.add(new Node(start));
		graphList = reverse(graphList);
		Node n = null;
		while (!stack.isEmpty()) {
			n = stack.pop();
			nodes.get(n.getNode()).setVisited(Boolean.TRUE);
			if (nodes.get(goal).isVisited()) {
				// System.out.println("Goal Found");
				// System.out.println("Cost:" + nodes.get(goal).getCost());
				backtrace(nodes, start, goal);
				return;
			}
			List<Node> list = graphList.get(n.getNode());
			if (list != null) {
				for (Node m : list) {
					if (!nodes.get(m.getNode()).isVisited()) {
						if (!nodeExists(m, stack)) {
							stack.add(m);
							nodes.get(m.getNode()).setParent(n.getNode());
							nodes.get(m.getNode()).setCost(
									nodes.get(n.getNode()).getCost() + 1);
						} else {
							updateNode(m, stack, nodes.get(n.getNode())
									.getCost() + 1, nodes, n);
						}
						// nodes.get(m.getNode()).setVisited(Boolean.TRUE);
					}
				}
			}
		}
		// System.out.println("No goal Found");
	}

	private static void updateNode(Node m, Stack<Node> stack, Integer pathCost,
			Map<String, Node> nodes, Node n2) {
		String nodeName = m.getNode();
		Stack<Node> tempStack = (Stack<Node>) stack.clone();
		Node n = null;
		while (!tempStack.isEmpty()) {
			n = tempStack.pop();
			if (n.getNode().equalsIgnoreCase(nodeName)
					&& pathCost < nodes.get(nodeName).getCost()) {
				nodes.get(nodeName).setParent(n2.getNode());
				nodes.get(nodeName).setCost(pathCost);
			}
		}
	}

	private static boolean nodeExists(Node m, Stack<Node> stack) {
		String nodeName = m.getNode();
		Stack<Node> tempStack = (Stack<Node>) stack.clone();
		Node n = null;
		while (!tempStack.isEmpty()) {
			n = tempStack.pop();
			if (n.getNode().equalsIgnoreCase(nodeName)) {
				return true;
			}
		}
		return false;
	}

	private static Map<String, List<Node>> reverse(
			Map<String, List<Node>> graphList) {
		for (String m : graphList.keySet()) {
			List list = graphList.get(m);
			ArrayList<Node> tmp = new ArrayList<Node>();
			for (ListIterator<Node> iter = list.listIterator(list.size()); iter
					.hasPrevious();) {
				tmp.add(iter.previous());
			}
			graphList.put(m, tmp);
		}
		return graphList;
	}

	private static void backtrace(Map<String, Node> nodes, String start,
			String goal) {
		Stack<Node> stack = new Stack<Node>();
		stack.add(nodes.get(goal));
		String key = nodes.get(goal).getParent();
		while (nodes.get(key).getParent() != null) {
			stack.add(nodes.get(key));
			key = nodes.get(key).getParent();
		}
		stack.add(nodes.get(key));
		Node n;
		File outputFile = new File("output.txt");

		try {
			FileWriter writer = new FileWriter(outputFile.getAbsoluteFile());
			BufferedWriter bufferWriter = new BufferedWriter(writer);
			while (!stack.isEmpty()) {
				n = stack.pop();
				bufferWriter.write(n.getNode() + " " + n.getCost() + "\n");
				System.out.println(n.getNode() + " " + n.getCost());
			}
			bufferWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void uniformCost(Map<String, List<Node>> graphList,
			Map<String, Node> nodes, String start, String goal) {
		System.out.println("running ucs");
		AtomicInteger numGenerator = new AtomicInteger();
		Comparator<Node> comparator = new Comparator<homework.Node>() {

			@Override
			public int compare(Node n, Node m) {
				int comparatorValue = n.getCost() - m.getCost();
				if (comparatorValue == 0
						&& n.getParent().equalsIgnoreCase(m.getParent())) {
					return (int) (nodes.get(n.getNode()).getOrder() - nodes
							.get(m.getNode()).getOrder());
				} else if (comparatorValue == 0
						&& !n.getParent().equalsIgnoreCase(m.getParent())) {
					return (int) (n.getSecorder() - m.getSecorder());
				}
				return comparatorValue;
			}
		};
		Queue<Node> priorityQueue = new PriorityQueue<Node>(comparator);
		// Integer pathCost = 0;
		priorityQueue.add(new Node(start));
		nodes.get(start).setVisited(Boolean.TRUE);
		nodes.get(start).setExplored(Boolean.TRUE);
		nodes.get(start).setCost(0);
		Node n = null;
		while (!priorityQueue.isEmpty()) {
			n = priorityQueue.poll();
			// if (!nodes.get(n.getNode()).getVisited()) {
			// nodes.get(n.getNode()).setCost(n.getCost());
			// nodes.get(n.getNode()).setParent(n.getParent());
			// } else if (nodes.get(n.getNode()).getCost() > n.getCost()) {
			nodes.get(n.getNode()).setCost(n.getCost());
			if (n.getParent() != null)
				nodes.get(n.getNode()).setParent(n.getParent());
			// }
			nodes.get(n.getNode()).setExplored(Boolean.TRUE);
			if (nodes.get(goal).isVisited() && nodes.get(goal).isExplored()) {
				// System.out.println("Goal Found");
				// System.out.println("Cost:" + nodes.get(goal).getCost());
				backtrace(nodes, start, goal);
				return;
			}
			List<Node> list = graphList.get(n.getNode());
			if (list != null) {
				for (Node m : list) {
					if (!nodes.get(m.getNode()).isVisited()) {
						m.setCost(n.getCost() + m.getTrafficdata());
						m.setParent(n.getNode());
						nodes.get(m.getNode()).setCost(m.getCost());
						nodes.get(m.getNode()).setParent(m.getParent());
						nodes.get(m.getNode()).setVisited(Boolean.TRUE);
						m.setSecorder(numGenerator.getAndIncrement());
						priorityQueue.add(m);
					} else if (nodes.get(m.getNode()).isVisited()
							&& !nodes.get(m.getNode()).isExplored()) {
						if (nodes.get(m.getNode()).getCost() > m
								.getTrafficdata() + n.getCost()) {
							// priorityQueue.remove(m);
							m.setCost(m.getTrafficdata() + n.getCost());
							m.setParent(n.getNode());
							nodes.get(m.getNode()).setCost(m.getCost());
							nodes.get(m.getNode()).setParent(m.getParent());
							priorityQueue = removeNode(m, priorityQueue, nodes);
							m.setSecorder(numGenerator.getAndIncrement());
							priorityQueue.add(m);
						}
					} else if (nodes.get(m.getNode()).isVisited()
							&& nodes.get(m.getNode()).isExplored()) {
						if (nodes.get(m.getNode()).getCost() > m
								.getTrafficdata() + n.getCost()) {
							// priorityQueue.remove(m);
							m.setCost(m.getTrafficdata() + n.getCost());
							m.setParent(n.getNode());
							nodes.get(m.getNode()).setCost(m.getCost());
							nodes.get(m.getNode()).setParent(m.getParent());
							nodes.get(m.getNode()).setExplored(Boolean.FALSE);
							priorityQueue = removeNode(m, priorityQueue, nodes);
							m.setSecorder(numGenerator.getAndIncrement());
							priorityQueue.add(m);
						}
					}

				}
			}
		}
		// System.out.println("No goal Found");
	}

	private static Queue<Node> removeNode(Node m, Queue<Node> priorityQueue,
			Map<String, Node> nodes) {
		Comparator<Node> comparator = new Comparator<homework.Node>() {

			@Override
			public int compare(Node n, Node m) {
				int comparatorValue = n.getCost() - m.getCost();
				if (comparatorValue == 0
						&& n.getParent().equalsIgnoreCase(m.getParent())) {
					return (int) (nodes.get(n.getNode()).getOrder() - nodes
							.get(m.getNode()).getOrder());
				} else if (comparatorValue == 0
						&& !n.getParent().equalsIgnoreCase(m.getParent())) {
					return (int) (n.getSecorder() - m.getSecorder());
				}
				return comparatorValue;
			}
		};
		Queue<Node> priorityQueuenew = new PriorityQueue<Node>(comparator);
		for (Node n : priorityQueue) {
			if (!n.getNode().equalsIgnoreCase(m.getNode()))
				priorityQueuenew.add(n);
		}
		return priorityQueuenew;
	}

	private static void AStar(Map<String, List<Node>> graphList,
			Map<String, Node> nodes, String start, String goal) {
		System.out.println("running AStar");
		AtomicInteger numGenerator = new AtomicInteger();
		Comparator<Node> comparator = new Comparator<homework.Node>() {

			@Override
			public int compare(Node n, Node m) {
				int comparatorValue = n.getTotalCost() - m.getTotalCost();
				if (comparatorValue == 0
						&& n.getParent().equalsIgnoreCase(m.getParent())) {
					return (int) (nodes.get(n.getNode()).getOrder() - nodes
							.get(m.getNode()).getOrder());
				} else if (comparatorValue == 0
						&& !n.getParent().equalsIgnoreCase(m.getParent())) {
					return (int) (n.getSecorder() - m.getSecorder());
				}
				return comparatorValue;
			}
		};
		Queue<Node> priorityQueue = new PriorityQueue<Node>(comparator);
		// Integer pathCost = 0;
		priorityQueue.add(new Node(start));
		nodes.get(start).setVisited(Boolean.TRUE);
		// nodes.get(start).setExplored(Boolean.TRUE);
		// nodes.get(start).setCost(0);
		Node n = null;
		while (!priorityQueue.isEmpty()) {
			n = priorityQueue.poll();
			// if (!nodes.get(n.getNode()).getExplored()) {
			nodes.get(n.getNode()).setCost(n.getCost());
			nodes.get(n.getNode()).setParent(n.getParent());
			nodes.get(n.getNode()).setTotalCost(n.getTotalCost());
			// } else if (nodes.get(n.getNode()).getTotalCost() >
			// n.getTotalCost()) {
			// nodes.get(n.getNode()).setCost(n.getCost());
			// nodes.get(n.getNode()).setParent(n.getParent());
			// nodes.get(n.getNode()).setTotalCost(n.getTotalCost());
			// }
			nodes.get(n.getNode()).setExplored(Boolean.TRUE);
			if (n.getParent() != null)
				nodes.get(n.getNode()).setParent(n.getParent());
			if (nodes.get(goal).isVisited() && nodes.get(goal).isExplored()) {
				// System.out.println("Goal Found");
				// System.out.println("Cost:" + nodes.get(goal).getCost());
				backtrace(nodes, start, goal);
				return;
			}
			List<Node> list = graphList.get(n.getNode());
			if (list != null) {
				for (Node m : list) {
					if (!nodes.get(m.getNode()).isVisited()) {
						m.setParent(n.getNode());
						m.setTotalCost(n.getCost() + m.getTrafficdata()
								+ nodes.get(m.getNode()).getTrafficFreedata());
						m.setCost(n.getCost() + m.getTrafficdata());
						nodes.get(m.getNode()).setCost(m.getCost());
						nodes.get(m.getNode()).setTotalCost(m.getTotalCost());
						// priorityQueue.add(m);
						nodes.get(m.getNode()).setVisited(Boolean.TRUE);
						m.setSecorder(numGenerator.getAndIncrement());
						priorityQueue.add(m);
					} else if (nodes.get(m.getNode()).isVisited()
							&& !nodes.get(m.getNode()).isExplored()) {
						if (nodes.get(m.getNode()).getTotalCost() > m
								.getTrafficdata()
								+ n.getCost()
								+ nodes.get(m.getNode()).getTrafficFreedata()) {
							m.setCost(m.getTrafficdata() + n.getCost());
							m.setTotalCost(m.getTrafficdata()
									+ n.getCost()
									+ nodes.get(m.getNode())
											.getTrafficFreedata());
							m.setParent(n.getNode());
							nodes.get(m.getNode()).setCost(m.getCost());
							nodes.get(m.getNode()).setTotalCost(
									m.getTotalCost());
							nodes.get(m.getNode()).setParent(m.getParent());
							priorityQueue = remove(m, priorityQueue, nodes);
							m.setSecorder(numGenerator.getAndIncrement());
							priorityQueue.add(m);
						}
					} else if (nodes.get(m.getNode()).isVisited()
							&& nodes.get(m.getNode()).isExplored()) {
						if (nodes.get(m.getNode()).getTotalCost() > m
								.getTrafficdata()
								+ n.getCost()
								+ nodes.get(m.getNode()).getTrafficFreedata()) {
							m.setCost(m.getTrafficdata() + n.getCost());
							m.setTotalCost(m.getTrafficdata()
									+ n.getCost()
									+ nodes.get(m.getNode())
											.getTrafficFreedata());
							m.setParent(n.getNode());
							nodes.get(m.getNode()).setCost(m.getCost());
							nodes.get(m.getNode()).setTotalCost(
									m.getTotalCost());
							nodes.get(m.getNode()).setParent(m.getParent());
							nodes.get(m.getNode()).setExplored(Boolean.FALSE);
							priorityQueue = remove(m, priorityQueue, nodes);
							m.setSecorder(numGenerator.getAndIncrement());
							priorityQueue.add(m);
						}
					}

				}
			}
		}
		// System.out.println("No goal Found");
	}

	private static Queue<Node> remove(Node m, Queue<Node> priorityQueue,
			Map<String, Node> nodes) {
		Comparator<Node> comparator = new Comparator<homework.Node>() {

			@Override
			public int compare(Node n, Node m) {
				int comparatorValue = n.getTotalCost() - m.getTotalCost();
				if (comparatorValue == 0
						&& n.getParent().equalsIgnoreCase(m.getParent())) {
					return (int) (nodes.get(n.getNode()).getOrder() - nodes
							.get(m.getNode()).getOrder());
				} else if (comparatorValue == 0
						&& !n.getParent().equalsIgnoreCase(m.getParent())) {
					return (int) (n.getSecorder() - m.getSecorder());
				}
				return comparatorValue;
			}
		};
		Queue<Node> priorityQueuenew = new PriorityQueue<Node>(comparator);
		for (Node n : priorityQueue) {
			if (!n.getNode().equalsIgnoreCase(m.getNode()))
				priorityQueuenew.add(n);
		}
		return priorityQueuenew;
	}

	public static void main(String[] args) {
		Map<String, List<Node>> graphList = new HashMap<String, List<Node>>();
		Map<String, Node> nodes = new HashMap<String, Node>();
		// Map<String, Integer> sundayTrafficFreeData=new
		// HashMap<String,Integer>();
		FileReader reader = null;
		Scanner in = null;
		
		try {
			reader = new FileReader("resources/input.txt");
			in = new Scanner(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (reader == null)
			return;
		String algorithm = in.next();
		String start = in.next();

		String goal = in.next();
		int lines = in.nextInt();
		in.nextLine();
		// System.out.println("start:" + start + " goal:" + goal + " lines:"
		// + lines);
		if (!nodes.containsKey(start))
			nodes.put(start, new Node(start));
		for (int i = 0; i < lines; i++) {
			// System.out.println("line is: "+in.nextLine());
			String begin = in.next();
			String dest = in.next();
			int val = in.nextInt();
			in.nextLine();
			if (!graphList.containsKey(begin)) {
				if (!nodes.containsKey(begin))
					nodes.put(begin, new Node(begin));

				if (!nodes.containsKey(dest))
					nodes.put(dest, new Node(dest));
				Node node = new Node(dest, val);
				List<Node> list = new LinkedList<Node>();
				list.add(node);
				graphList.put(begin, list);
			} else {
				if (!nodes.containsKey(dest)) {
					nodes.put(dest, new Node(dest));
				}
				graphList.get(begin).add(new Node(dest, val));
			}
			// System.out.println("Key:" + begin + "adacency:"
			// + graphList.get(begin));
		}
		lines = in.nextInt();
		in.nextLine();
		for (int i = 0; i < lines; i++) {
			String source = in.next();
			int val = in.nextInt();
			if (in.hasNextLine())
				in.nextLine();
			if (!nodes.containsKey(source))
				nodes.put(source, new Node(source));
			nodes.get(source).setTrafficFreedata(val);
		}
		// System.out.println("Nodes:" + nodes.entrySet());
		if (goal.equalsIgnoreCase(start)) {
			File outputFile = new File("output.txt");

			try {
				FileWriter writer = new FileWriter(outputFile.getAbsoluteFile());
				BufferedWriter bufferWriter = new BufferedWriter(writer);
				bufferWriter.write(start + " 0\n");
				bufferWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(start + " 0");
		} else {
			if ("bfs".equalsIgnoreCase(algorithm))
				bfs(graphList, nodes, start, goal);
			else if ("dfs".equalsIgnoreCase(algorithm))
				dfs(graphList, nodes, start, goal);
			else if ("ucs".equalsIgnoreCase(algorithm))
				uniformCost(graphList, nodes, start, goal);
			else
				AStar(graphList, nodes, start, goal);
		}
	}
}
