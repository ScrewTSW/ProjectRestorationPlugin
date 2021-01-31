public class Logger {

    public static LogLevel LOG_LEVEL = LogLevel.INFO;
    private static String pluginName;
    private static Plugin parent = null;
    private static Logger logger = null;

    private Logger(LogLevel logLevel, Plugin parent) {
        Logger.LOG_LEVEL = logLevel;
        if (parent == null) {
            Logger.pluginName = "[Logger]";
        } else {
            Logger.parent = parent;
            Logger.pluginName = "[" + Logger.parent.getName() + "]";
        }
    }

    public static Logger getInstance(LogLevel logLevel, Plugin parent) {
        if (Logger.logger == null) {
            Logger.logger = new Logger(logLevel, parent);
        }
        return Logger.logger;
    }

    public static Logger getInstance(LogLevel logLevel) {
        return getInstance(logLevel, null);
    }

    public static Logger getInstance() {
        return getInstance(LogLevel.INFO);
    }

    public static void error(String str) {
        if (Logger.logger == null) Logger.getInstance();
        if (Logger.LOG_LEVEL == LogLevel.TRACE ||
                Logger.LOG_LEVEL == LogLevel.DEBUG ||
                Logger.LOG_LEVEL == LogLevel.INFO ||
                Logger.LOG_LEVEL == LogLevel.WARN ||
                Logger.LOG_LEVEL == LogLevel.ERROR)
        System.out.println(Logger.pluginName + " [ERROR] " + str);
    }

    public static void warn(String str) {
        if (Logger.logger == null) Logger.getInstance();
        if (Logger.LOG_LEVEL == LogLevel.TRACE ||
                Logger.LOG_LEVEL == LogLevel.DEBUG ||
                Logger.LOG_LEVEL == LogLevel.INFO ||
                Logger.LOG_LEVEL == LogLevel.WARN)
        System.out.println(Logger.pluginName + " [WARN] " + str);
    }

    public static void info(String str) {
        if (Logger.logger == null) Logger.getInstance();
        if (Logger.LOG_LEVEL == LogLevel.TRACE ||
                Logger.LOG_LEVEL == LogLevel.DEBUG ||
                Logger.LOG_LEVEL == LogLevel.INFO)
        System.out.println(Logger.pluginName + " [INFO] " + str);
    }

    public static void debug(String str) {
        if (Logger.logger == null) Logger.getInstance();
        if (Logger.LOG_LEVEL == LogLevel.TRACE ||
                Logger.LOG_LEVEL == LogLevel.DEBUG)
        System.out.println(Logger.pluginName + " [DEBUG] " + str);
    }

    public static void trace(String str) {
        if (Logger.logger == null) Logger.getInstance();
        if (Logger.LOG_LEVEL == LogLevel.TRACE)
        System.out.println(Logger.pluginName + " [TRACE] " + str);
    }

    public enum LogLevel{
        ERROR("ERROR"),
        WARN("WARN"),
        INFO("INFO"),
        DEBUG("DEBUG"),
        TRACE("TRACE");

        private String level;

        LogLevel(String level) {
            this.level = level;
        }

        public static LogLevel parse(String level) {
            switch(level) {
                case "ERROR": return ERROR;
                case "WARN": return WARN;
                case "INFO": return INFO;
                case "DEBUG": return DEBUG;
                case "TRACE": return TRACE;
                default: return DEBUG;
            }
        }

        public String getValue() {
            return this.level;
        }
    }
}
