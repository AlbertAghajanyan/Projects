export const serverPort = 4300;
export const secret = "RbBQqA6uF#msRF8s7h*?@=95HUm&DgMDd6zLFn4XzWQ6dtwXSJwBX#?gL2JWf!";
export const length = 128;
export const digest = "sha256";

export const devProd = {
    development: {
        db: {
            database: "intrusion_detector_db",
            host: "localhost",
            password: "root",
            port: 5432,
            user: "postgres",
        },
        server: {
            database: "intrusion_detector_db",
            host: "localhost",
            password: "root",
            port: 5432,
            user: "postgres",
        },
    },
    production: {
        db: {
            database: "intrusion_detector_db",
            host: "localhost",
            password: "root",
            port: 5432,
            user: "postgres",
        },
        server: {
            database: "intrusion_detector_db",
            host: "localhost",
            password: "root",
            port: 5432,
            user: "postgres",
        },
    },
};
