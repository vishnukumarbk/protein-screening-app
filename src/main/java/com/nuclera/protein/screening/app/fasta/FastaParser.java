package com.nuclera.protein.screening.app.fasta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class FastaParser {

    private FastaParser() {
    }

    public static List<String> parse(final String fasta) {
        final List<String> sequences = new ArrayList<>();
        final List<String> lines = fasta.lines().toList();
        String header = null;
        StringBuilder sequence = null;

        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i).strip();
            if (line.isEmpty() || line.startsWith(";")) {
                continue;
            }
            if (line.startsWith(">")) {
                if (header != null) {
                    sequences.add(finish(header, sequence));
                }
                header = line;
                sequence = new StringBuilder();
            } else if (header == null) {
                throw new InvalidFastaException("line " + (i + 1) + " has sequence data before the first '>' header");
            } else {
                sequence.append(line.replaceAll("\\s+", "").toUpperCase(Locale.ROOT));
            }
        }
        if (header != null) {
            sequences.add(finish(header, sequence));
        }
        return sequences;
    }

    private static String finish(final String header, final StringBuilder sequence) {
        if (sequence.isEmpty()) {
            throw new InvalidFastaException("record '" + header + "' has no sequence");
        }
        return sequence.toString();
    }
}
