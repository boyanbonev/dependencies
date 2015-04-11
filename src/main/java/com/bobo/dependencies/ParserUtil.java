package com.bobo.dependencies;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;

/**
 * An utility class used for reading and parsing the input from various sources.
 *
 * @author boyan
 *
 */
public final class ParserUtil {

    private ParserUtil() {
    }

    /**
     * Parses the input coming from a file. The input is the following format: <br>
     * A set of lines where the first token is the name of an item. The
     * remaining tokens are the names of things that this first item depends on. <br>
     * Ex.:<br>
     * <br>
     * A B C<br>
     * <br>
     * B C E<br>
     * <br>
     * C G<br>
     *
     * @param inpFilePath
     *            path to the file containing the input data
     * @return a Map whose key is the element, and value is the list of the
     *         direct dependencies
     * @throws IOException
     *             If an I/O error occurs while reading from the stream
     */
    public static Map<String, List<String>> readInput(String inpFilePath) throws IOException {
        Validate.notBlank(inpFilePath);

        try (Reader reader = new FileReader(inpFilePath)) {
            return readInput(reader);
        }
    }

    /**
     * {@link #readInput(String)}
     *
     * @param reader
     *            and input stream reader from where the input will be read
     * @throws IOException
     *             If an I/O error occurs while reading from the stream
     */
    public static Map<String, List<String>> readInput(Reader inpReader) throws IOException {
        Validate.notNull(inpReader);

        BufferedReader reader = new BufferedReader(inpReader);
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] split = line.trim().split("\\s");
            List<String> elements = new ArrayList<String>(split.length > 0 ? split.length - 1 : 0);
            for (int i = 1; i < split.length; i++) {
                elements.add(split[i]);
            }
            result.put(split[0], elements);
        }

        return result;
    }

    public static void printData(Map<String, ? extends Collection<String>> data) {
        for (Map.Entry<String, ? extends Collection<String>> entry : data.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}
