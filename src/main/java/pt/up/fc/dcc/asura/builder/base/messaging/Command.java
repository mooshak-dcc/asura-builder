package pt.up.fc.dcc.asura.builder.base.messaging;

import pt.up.fc.dcc.asura.builder.base.exceptions.BuilderException;

import java.util.Arrays;

/**
 * Commands sent from players to execute actions in the game
 *
 * @author José Paulo Leal <code>zp@dcc.fc.up.pt</code>
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public class Command {

    private String name;
    private Object[] args;

    public Command(String name, Object... args) {
        this.name = name;
        this.args = args;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the args
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * @param args the args to set
     */
    public void setArgs(Object[] args) {
        this.args = args;
    }

    /**
     * @param arg the index of the argument
     * @return the string value of the argument
     */
    public String getAsString(int arg) throws BuilderException {

        if (args.length <= arg)
            throw new BuilderException(
                    String.format("Argument %d of command %s in not defined.", arg, name));

        try {
            return String.valueOf(args[arg]);
        } catch (NumberFormatException e) {
            throw new BuilderException(
                    String.format("Argument %d of command %s in not an int.", arg, name));
        }
    }

    /**
     * @param arg the index of the argument
     * @return the integer value of the argument
     */
    public int getAsInt(int arg) throws BuilderException {

        if (args.length <= arg)
            throw new BuilderException(
                    String.format("Argument %d of command %s in not defined.", arg, name));

        try {
            return Integer.parseInt(String.valueOf(args[arg]));
        } catch (NumberFormatException e) {
            throw new BuilderException(
                    String.format("Argument %d of command %s in not an int.", arg, name));
        }
    }

    /**
     * @param arg the index of the argument
     * @return the float value of the argument
     */
    public float getAsFloat(int arg) throws BuilderException {

        if (args.length <= arg)
            throw new BuilderException(
                    String.format("Argument %d of command %s in not defined.", arg, name));

        try {
            return Float.parseFloat(String.valueOf(args[arg]));
        } catch (NumberFormatException e) {
            throw new BuilderException(
                    String.format("Argument %d of command %s in not a float.", arg, name));
        }
    }

    /**
     * @param arg the index of the argument
     * @return the double value of the argument
     */
    public double getAsDouble(int arg) throws BuilderException {

        if (args.length <= arg)
            throw new BuilderException(
                    String.format("Argument %d of command %s in not defined.", arg, name));

        try {
            return Double.parseDouble(String.valueOf(args[arg]));
        } catch (NumberFormatException e) {
            throw new BuilderException(
                    String.format("Argument %d of command %s in not a double.", arg, name));
        }
    }

    /**
     * @param arg the index of the argument
     * @return the boolean value of the argument
     */
    public boolean getAsBoolean(int arg) throws BuilderException {

        if (args.length <= arg)
            throw new BuilderException(
                    String.format("Argument %d of command %s in not defined.", arg, name));

        try {
            return Boolean.parseBoolean(String.valueOf(args[arg]));
        } catch (NumberFormatException e) {
            throw new BuilderException(
                    String.format("Argument %d of command %s in not a boolean.", arg, name));
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Command [name=" + name + ", args=" + Arrays.toString(args) + "]";
    }
}
