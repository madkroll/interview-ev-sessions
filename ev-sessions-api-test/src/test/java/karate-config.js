function() {
    // don't waste time waiting for a connection or if servers don't respond within 1 minute
    karate.configure('connectTimeout', 60000);
    karate.configure('readTimeout', 60000);

    var baseUrl = karate.properties['baseUrl'];

    if (!baseUrl) {
        karate.error('FATAL: Java System Property is missing: baseUrl.', baseUrl);
    }

    var config = {
        baseUrl: baseUrl
    };

    return config;
}
