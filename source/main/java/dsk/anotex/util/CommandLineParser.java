package dsk.anotex.util;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Parser for command line arguments. Expected format:
 * <pre>-arg1 argValue1 -arg2 value3</pre>
 * <p>
 * This implementation fits in single class! There are enough java libraries for parsing command line.
 * Most of them are example for over-engineering (but it is fun to see how much code was written just
 * to parse array of strings).
 * </p>
 */
public class CommandLineParser {
    protected Map<String, String> args;
    protected List<String> values;
    protected String argPrefix;

    /**
     * Create empty instance.
     */
    public CommandLineParser() {
        super();
        values = new LinkedList<>();
        argPrefix = "-";
    }

    /**
     * Constructor with specified parameters.
     * @param args Command line arguments.
     */
    public CommandLineParser(String[] args) {
        this();
        parse(args);
    }

    /**
     * Parse specified command line arguments.
     * @param arguments Command line arguments.
     */
    public void parse(String[] arguments) {
        args = parseArguments(arguments);
    }

    /**
     * Check if specified argument was passed.
     * @param name Argument name.
     * @return True if this argument exists.
     */
    public boolean hasArgument(String name) {
        return args.containsKey(name);
    }

    /**
     * Get argument with specified name.
     * @param name Argument name.
     * @return Argument value or null.
     *
     * @see #parse(String[])
     */
    public String getArgumentValue(String name) {
        return getArgumentValue(name, null);
    }

    /**
     * Get argument with specified name.
     * @param name Argument name.
     * @param defaultValue Default value.
     * @return Argument value or the default value.
     *
     * @see #parse(String[])
     */
    public String getArgumentValue(String name, String defaultValue) {
        String ret = args.get(name);
        if (ret == null) {
            ret = defaultValue;
        }
        return ret;
    }

    /**
     * Get parsed command line arguments.
     * @return Parsed arguments.
     *
     * @see #parse(String[])
     */
    @SuppressWarnings("unused")
    public Map<String, String> getArguments() {
        return args;
    }

    /**
     * Get the command line values without the arguments. Example:
     * <pre> 'command -arg1 v1 arg2' </pre>
     * The result will be [v1, arg2].
     * @return The value without argument name.
     */
    @SuppressWarnings("unused")
    public List<String> getValues() {
        return values;
    }

    /**
     * Parse command line arguments (parameters). Parameters which contains spaces should be
     * enclosed with double quotas. Double quote sign itself (if present in command value) should
     * be escaped with \.
     * @param args Command line arguments, passed to the application.
     *
     * @return Map with parsed keys and values. Or empty map (if no command line options passed).
     */
    public Map<String, String> parseArguments(String[] args) {
        LinkedHashMap<String, String> arguments = new LinkedHashMap<>();
        if ((args == null) || (args.length == 0)) {
            // No parameters passed.
            return arguments;
        }

        for (int i = 0; i < args.length; i++) {
            String s = args[i];
            if ((s == null) || (s.isEmpty())) {
                // Invalid argument. Skip it.
                continue;
            }

            String sOption = null;
            String sValue = null;
            if (s.startsWith(argPrefix)) {
                // It is argument.
                sOption = s.substring(argPrefix.length());
                if (args.length - i > 1) {
                    // Argument value.
                    String ss = args[i + 1];
                    if (!ss.startsWith(argPrefix)) {
                        sValue = ss;
                        i++;
                    }
                }
            }
            else {
                // It is value.
                sValue = s;
            }

            // Remove value enclosing quotas (if any).
            final String dQuota = "\"";
            final String sQuota = "'";
            if ((sValue != null) && (sValue.startsWith(sQuota))) {
                if (((sValue.startsWith(dQuota) && sValue.endsWith(dQuota))) ||
                    ((sValue.startsWith(sQuota) && sValue.endsWith(sQuota)))) {
                    sValue = sValue.substring(1, sValue.length() - 1);
                }
            }

            // Add to parameter map.
            if (sOption != null) {
                arguments.put(sOption, sValue);
            }
            if (sValue != null) {
                values.add(sValue);
            }
        } //

        return arguments;
    }
}
