import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileReader;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;

public class BotTester {
	
	private static byte[][] images;
	private static byte[] labels;
	private static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
	private static Bot[] bots;
	private static int run = 0;
	private static float change = 0.01f;
	private static NumberDisplay display;
	private static JLabel numtxt;
	
	public static void main(String[] args) throws IOException {
		
		if (args != null) {
			
			for (String elem: args) {
				
				parseCommand(elem);
				
			}
			
		}
		
		JFrame frame = new JFrame(":hideriBlob:");
		JPanel panel = new JPanel();
		display = new NumberDisplay();
		numtxt = new JLabel(":hideriBlob:");
		frame.setSize(112, 256);
		frame.setLocation(100, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		panel.setLayout(new BorderLayout());
		panel.add(display, BorderLayout.NORTH);
		panel.add(numtxt, BorderLayout.SOUTH);
		numtxt.setFont(new Font("Arial", Font.PLAIN, 32));
		frame.setContentPane(display);
		System.out.print("AI> ");
		String command = stdin.readLine();
		
		while (parseCommand(command)) {
			
			System.out.print("AI> ");
			command = stdin.readLine();
			
		}
		
		System.exit(0);
		
	}
	
	private static boolean parseCommand(String command) throws IOException {
		
		if (command == null) {
			
			command = "";
			
		}
		
		switch (command.split(" ")[0]) {
			
			case "?": case "help":
				System.out.println("?	cycle	display	exit	help	makeBots	purge");
				System.out.println("printBot	printBots	readBot	setChange	testBots");
				return true;
				//break;
				
			case "makeBots":
				makeBots();
				return true;
				//break;
				
			case "display":
				display(command);
				return true;
				//break;
				
			case "exit":
				System.out.println("Byeeeeeeeeeeeeee");
				// out.close();
				return false;
				//break;
				
			case "testBots":
				testBots();
				return true; 
				//break;
				
			case "cycle":
				cycle(command);
				return true;
				//break;
				
			case "printBots":
				printBots();
				return true;
				//break;
				
			case "printBot":
				printBot();
				return true;
				//break;
				
			case "readBot":
				readBot(command);
				return true;
				//break;
				
			case "setChange":
				setChange(command);
				return true;
				//break;
				
			case "purge":
				purge();
				return true;
				
			default:
				System.out.println("Not a command.");
				return true;
				//break;
				
		}
		
	}
	
	private static void makeBots() throws IOException {
		
		if (bots == null || bots[0] == null) {
			
			bots = new Bot[1000];
			
			for (int i = 0; i < 1000; i++) {
				
				bots[i] = new Bot();
				
			}
			
		} else if (bots[0].accuracy == 0) {
			
			bots = null;
			makeBots();
			
		} else {
			
			for (int i = 0; i < 10; i++) {
				
				bots[i].spawnBots(bots, i);
				
			}
			
		}
		
		run++;
		
	}
	
	private static void testBots() throws IOException {
		
		if (images == null) {
			
			BufferedInputStream imageFile = new BufferedInputStream(new FileInputStream("/home/guninvalid/images"));
			imageFile.read(new byte[16]);
			images = new byte[60000][784];
			
			for (byte[] elem: images) {
				
				imageFile.read(elem);
				
			}
			
			imageFile.close();
			
		}
		
		if (labels == null) {
			
			BufferedInputStream labelsFile = new BufferedInputStream(new FileInputStream("/home/guninvalid/labels"));
			labelsFile.read(new byte[8]);
			labels = new byte[60000];
			labelsFile.read(labels);
			labelsFile.close();
			
		}
		
		for (Bot elem: bots) {
			
			for (int i = 0; i < 784; i++) {
				
				elem.accuracy += (double) Math.abs(elem.read(images[i]) - labels[i]);
				
			}
			
		}
		
		for (int i = 0; i < 1000; i++) {
			
			bots[i].num = i;
			
		}
		
		Arrays.sort(bots);
		
		printBots();
		
	}
	
	private static void printBots() throws IOException {
		
		float sum = 0.0f;
		
		for (Bot elem: bots) {
			
			sum += elem.accuracy;
			
		}
		
		System.out.println("=== Run #" + String.valueOf(run) + " ===");
		System.out.println("Number of bots: 1000");
		System.out.println("Average: 	" + String.valueOf(sum / 1000f));
		System.out.println("Range: 		" + String.valueOf(bots[999].accuracy - bots[0].accuracy));
		System.out.println("Max: 		" + bots[999].toString());
		System.out.println("Q3: 		" + String.valueOf((bots[749].accuracy + bots[750].accuracy) / 2.0f));
		System.out.println("Median: 	" + String.valueOf((bots[499].accuracy + bots[500].accuracy) / 2.0f));
		System.out.println("Q1: 		" + String.valueOf((bots[249].accuracy + bots[250].accuracy) / 2.0f));
		System.out.println("Min: 		" + bots[0].toString());
		System.out.println("Top 10: ");
		
		for (int i = 9; i >= 0; i--) {
			
			System.out.println(bots[i].toString());
			
		}
		
	}
	
	private static void cycle(String command) throws IOException {
		
		int cycles = 1;
		
		try {
			
			cycles = Integer.parseInt(command.split(" ")[1]);
			
		} catch (NumberFormatException | IndexOutOfBoundsException ex) {
			
			System.out.println("Number not given or malformed. Cycling 1 time.");
			
		}
		
		for (; cycles > 0; cycles--) {
			
			makeBots();
			testBots();
			
		}
		
	}
	
	private static void printBot() throws IOException {
		
		System.out.print("Printing best bot to /var/bot...");
		
		try {
			
			PrintWriter writer = new PrintWriter("/var/bot");
			
			for (double elem: bots[0].multipliers) {
				
				writer.println(String.valueOf(elem));
				
			}
			
			writer.close();
			
		} catch (FileNotFoundException ex) {
			
			System.out.println("\n/var/bot does not already exist.");
			
		}
		
		System.out.println("Done");
		
	}
	
	private static void readBot(String filename) throws IOException {
		
		try {
			
			filename = filename.split(" ")[1];
			
		} catch (ArrayIndexOutOfBoundsException ex) {
			
			filename = "/var/bot";
			
		}
		
		System.out.println("Reading bot from " + filename + "...");
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		Bot bot = new Bot();
		try {
			
			for (int i = 0; i < 784; i++) {
				
				bot.multipliers[i] = Float.parseFloat(reader.readLine());
				
			}
			
			reader.close();
			
			for (int i = 0; i < 1000; i++) {
				
				bots[i] = bot;
				
			}
			
		} catch (FileNotFoundException ex) {
			
			System.out.println("\nFile not found.");
			reader.close();
			
		}
		
		System.out.println("Done");
		
	}
	
	private static void setChange(String command) throws IOException {
		
		try {
			
			float tmp = Float.parseFloat(command.split(" ")[1]);
			change = tmp;
			System.out.println("Change changed to " + String.valueOf(change) + ".");
			
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
			
			System.out.println("Number malformed or nonexistent. Change unchanged.");
			
		}
		
	}
	
	private static void purge() throws IOException {
		
		bots = new Bot[784];
		
	}
	
	private static void display(String cmd) throws IOException {
		
		String[] split = cmd.split(" ");
		int img;
		
		if (1 < split.length) {
			
			img = display.img + 1;
			System.out.println("No image number specified. Falling back to " + String.valueOf(img));
			
		} else {
			
			img = Integer.parseInt(split[1]);
			
		}
		
		display.display(img);
		
	}
	
	private static class Bot implements Comparable<Bot> {
		
		public float[] multipliers;
		public double accuracy;
		public int num;
		
		Bot() {
			
			multipliers = new float[784];
			accuracy = 0;
			num = -1;
			
			for (int i = 0; i < 784; i++) {
				
				multipliers[i] = (float) (Math.random() * 2.0) - 1.0f;
				
			}
			
		}
		
		Bot(Bot pbot) {
			
			multipliers = new float[784];
			accuracy = 0;
			num = -1;
			
			for (int i = 0; i < 784; i++) {
				
				this.multipliers[i] = pbot.multipliers[i] + ((float) (Math.random() * change)) - (change / 2.0f);
				
			}
			
		}
		
		public void spawnBots(Bot[] bots, int index) {
			
			Bot seedBot = bots[index];
			int endIndex = index * 100 + 100;
			
			for (int i = index * 100; i < endIndex; i++) {
				
				bots[i] = new Bot(seedBot);
				
			}
			
		}
		
		public float read(byte[] image) throws IOException {
			
			float sum = 0;
			
			for (int i = 0; i < 784; i++) {
				
				sum += (float) image[i] * multipliers[i];
				
			}
			
			return sum;
			
		}
		
		public int compareTo(Bot bot) {
			
			if (this.accuracy > bot.accuracy) {
				
				return 1;
				
			} else if (this.accuracy == bot.accuracy) {
				
				return 0;
				
			}
			
			return -1;
			
		}
		
		public String toString() {
			
			return String.valueOf(num) + "	" + String.valueOf(accuracy);
			
		}
		
	}
	
	private static class NumberDisplay extends JPanel {
		
		public int img;
		private BufferedImage bucket;
		private Graphics2D bmp;
		
		public NumberDisplay() {
			
			super();
			bucket = new BufferedImage(112, 112, BufferedImage.TYPE_INT_RGB);
			bmp = bucket.createGraphics();
			
		}
		
		public void paintComponent(Graphics g) {
			
			g.drawImage(bucket, 0, 0, 112, 112, null);
			
		}
		
		public void display(int pimg) {
			
			img = pimg;
			
			for (int i = 0; i < 784; i++) {
				
				bmp.setColor(new Color(images[img][i], images[img][i], images[img][i]));
				bmp.fillRect((int) Math.floor((double) i / 28.0), i % 28, 4, 4);
				
			}
			
			numtxt.setText(String.valueOf(labels[img]));
			
		}
		
	}
	
}
