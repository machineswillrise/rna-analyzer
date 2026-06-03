import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

enum Codon {
	PHENYALANINE("Phenylanine"),
	LEUCINE("Leucine"),
	ISOLEUCINE("Isoleucine"),
	METHIONINE("Methionine"),
	VALINE("Valine"),
	TYROSINE("Tyrosine"),
	STOP("Stop"),
	HISTIDINE("Histidine"),
	GLUTAMINE("Glutamine"),
	ASPARAGINE("Asparagine"),
	LYSINE("Lysine"),
	ASPARTIC_ACID("Aspartic acid"),
	GLUTAMIC_ACID("Glutamic acid"),
	CYSTEINE("Cysteine"),
	ARGININE("Arginine"),
	GLYCINE("Glycine"),
	UNKNOWN("Unknown");

	private final String name;

	private Codon(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

class RNAAnalyzer {
	private static final Map<Character, Character> CONVERSIONS = new HashMap<>() {{
		put('A', 'U');
		put('C', 'G');
		put('T', 'A');
		put('G', 'C');
	}};

	private static final JFrame frame = new JFrame();
	private static final JPanel panel = new JPanel();

	private static String getCodon(String rnaSegment) {
		return switch (rnaSegment) {
			case "UUU", "UUC"               -> Codon.PHENYALANINE.getName();
			case "UUA", "UUG"               -> Codon.LEUCINE.getName();
			case "CUU", "CUC", "CUA", "CUG" -> Codon.LEUCINE.getName();
			case "AUU", "AUC", "AUA"        -> Codon.ISOLEUCINE.getName();
			case "AUG"                      -> Codon.METHIONINE.getName();
			case "GUU"                      -> Codon.VALINE.getName();
			case "UAU", "UAC"               -> Codon.TYROSINE.getName();
			case "UAA", "UAG", "UGA"        -> Codon.STOP.getName();
			case "CAU", "CAC"               -> Codon.HISTIDINE.getName();
			case "CAA", "CAG"               -> Codon.GLUTAMINE.getName();
			case "AAU", "AAC"               -> Codon.ASPARAGINE.getName();
			case "AAA", "AAG"               -> Codon.LYSINE.getName();
			case "GAU", "GAC"               -> Codon.ASPARTIC_ACID.getName();
			case "GAA", "GAG"               -> Codon.GLUTAMIC_ACID.getName();
			case "UGU", "UGC"               -> Codon.CYSTEINE.getName();
			case "CGU", "CGC", "CGA", "CGG" -> Codon.ARGININE.getName();
			case "GGU", "GGC", "GGA", "GGG" -> Codon.GLYCINE.getName();
			default                         -> Codon.UNKNOWN.getName();
		};
	}

	private static String formatCodons(List<String> segments) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < segments.size(); i++) {
			String segment = segments.get(i);
			String codon = getCodon(segment);
			result.append(codon);

			if (codon.equals(Codon.STOP.getName())) {
				result.append("\n");
				break;
			}

			if (i < segments.size() - 1) {
				result.append(" - ");
			} else {
				result.append("\n");
			}
		}

		return result.toString();
	}

	private static void add(Box box, JComponent... components) {
		for (JComponent component : components) {
			box.add(component);
		}
	}

	private static void setUpPanel(JPanel panel) {
		Box box = Box.createVerticalBox();
		JTextField rna = new JTextField(20);
		JButton ok = new JButton("OK");
		JLabel dna = new JLabel("DNA sequence: ");
		JLabel segments = new JLabel("Segments: ");

		add(box, rna, ok, dna, segments);
		panel.add(box);

		ok.addActionListener(e -> {
			String rnaText = rna.getText();
			char[] sequence = rnaText.toCharArray();
			char[] result = new char[sequence.length];

			boolean valid = true;
			for (int i = 0; i < sequence.length; i++) {
				char current = sequence[i];
				if (!CONVERSIONS.containsKey(current)) {	
					dna.setText("Invalid DNA sequence");
					valid = false;
					break;
				}

				result[i] = CONVERSIONS.get(current);
			}

			String fullResult = new String(result);
			if (valid) {
				dna.setText(fullResult);
			}

			List<String> segmentsList = new ArrayList<>();
			int length = fullResult.length();

			for (int i = 0; i < length; i += 3) {
				segmentsList.add(fullResult.substring(i, Math.min(length, i + 3)));
			}

			segments.setText("Segments: " + formatCodons(segmentsList));
		});
	}

	public RNAAnalyzer() {
		frame.setSize(384, 128);
		frame.setTitle("RNA Analyzer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setUpPanel(panel);
		frame.add(panel);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new RNAAnalyzer();
		});
	}
}
