import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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

	private static boolean isLast(List<String> strings, String someString) {
		return someString.equals(strings.get(strings.size() - 1));
	}

	private static void printOutput(List<String> segments) {
		for (String segment : segments) {
			String codon = getCodon(segment);
			System.out.print(codon);

			if (codon.equals(Codon.STOP.getName())) {
				if (isLast(segments, segment)) {
					System.out.println();
				} else {
					System.out.println("Stop");
				}
				break;
			}

			if (isLast(segments, segment)) {
				System.out.println();
			} else {
				System.out.print(" - ");
			}
		}
	}
	
	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				System.out.print("Please enter an RNA sequence: ");
				String rna = scanner.nextLine();

				char[] sequence = rna.toCharArray();
				char[] dna = new char[sequence.length];

				boolean valid = true;
				for (int i = 0; i < sequence.length; i++) {
					char current = sequence[i];
					if (!CONVERSIONS.containsKey(current)) {	
						System.out.println("Invalid RNA sequence.");
						valid = false;
						break;
					}

					dna[i] = CONVERSIONS.get(current);
				}

				if (valid) {
					String fullDna = new String(dna);
					System.out.println("DNA sequence: " + fullDna);

					List<String> segments = new ArrayList<>();
					int length = fullDna.length();

					for (int i = 0; i < length; i += 3) {
						segments.add(fullDna.substring(i, Math.min(length, i + 3)));
					}

					printOutput(segments);
				}
			}
		}
	}
}