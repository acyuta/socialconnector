package kim.glushkova.socialconnector;

public class AuthenticationResult {

    public static Builder create(String provider) {
        return new Builder(provider);
    }

    private final ResultStatus status;
    private final String message;
    private final String token;
    private final String provider;

    private AuthenticationResult(String provider, ResultStatus status, String message, String token) {
        this.provider = provider;
        this.status = status;
        this.message = message;
        this.token = token;
    }

    public String getProvider() {
        return provider;
    }

    public ResultStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }


    public static class Builder {
        private final String provider;
        private ResultStatus status;
        private String message = "";
        private String token;

        public Builder(String provider) {
            this.provider = provider;
        }

        public Builder status(ResultStatus status) {
            this.status = status;
            this.message = status.name();
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public AuthenticationResult build() {
            return new AuthenticationResult(provider, status, message, token);
        }
    }
}
