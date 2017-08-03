package agent;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * <code>AgentLoader</code> class loads a <code>Java</code> agent in the
 * <code>Java Virtual Machine</code>.
 *
 * @author Juan-Alberto Hern&aacute;ndez-Mart&iacute;nez
 * @version %I%, %G%
 */
public class AgentLoader {

    /**
     * Gets the path of the <code>JAR</code> file corresponding to the <code>Java</code> agent.
     *
     * @return the agent path.
     */
    private static String getAgentPath() {
        String agentPath = System.getProperty("user.dir") + File.separator + "lib" + File.separator + "syntixiAgent.jar";

        System.out.println("[Syntixi Agent]\tPath:\t" + agentPath);

        return agentPath;
    }

    /**
     * Loads the <code>Java</code> agent in the <code>Java Virtual Machine</code>.
     */
    public static void loadAgent() {
        System.out.println("[Syntixi Agent]\tLoading agent");

        String beanName = ManagementFactory.getRuntimeMXBean().getName();
        int endIndex = beanName.indexOf('@');
        String pid = beanName.substring(0, endIndex);

        System.out.println("[Syntixi Agent]\tCurrent JVM:\t" + beanName + "\tPid:\t" + pid);

        try {
            VirtualMachine virtualMachine = VirtualMachine.attach(pid);

            virtualMachine.loadAgent(getAgentPath(), null);
            virtualMachine.detach();
        }
        catch(AttachNotSupportedException | IOException | AgentLoadException | AgentInitializationException e) {
            throw new RuntimeException(e);
        }
    }
}