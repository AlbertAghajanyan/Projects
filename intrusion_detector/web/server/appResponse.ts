export const APP_RESPONSES = {
    development: {
        created: {
            body: {
                code: 61,
                message: "Created successfuly",
            },
            httpCode: 201,
        },
        deleted: {
            body: {
                code: 62,
                message: "Deleted successfuly",
            },
            httpCode: 204,
        },
        edited: {
            body: {
                code: 63,
                message: "Edited successfuly",
            },
            httpCode: 204,
        },
        incorrect_body_param: {
            body: {
                code: 33,
                message: "Provided body parameter is incorrect. The following key(s) is/are missing or incorrect.",
            },
            httpCode: 400,
        },
        incorrect_db_error: {
            body: {
                code: 145,
                message: "A database issue appeared:",
            },
            httpCode: 400,
        },
        incorrect_device_id: {
            body: {
                code: 31,
                message: "Incorrect param: There is no device with specified id.",
            },
            httpCode: 400,
        },
        incorrect_object_id: {
            body: {
                code: 32,
                message: "Incorrect param: There is no object with specified id.",
            },
            httpCode: 400,
        },
        incorrect_param: {
            body: {
                code: 30,
                message: "Provide query parameters are incorrect: ",
            },
            httpCode: 400,
        },
        internal_dbs_error: {
            body: {
                code: 21,
                message: "Database down.",
            },
            httpCode: 500,
        },
        internal_error: {
            body: {
                code: 20,
                message: "An unknown internal error occurred.",
            },
            httpCode: 500,
        },
        invalid_token: {
            body: {
                code: 80,
                message: "The access token used in the request is incorrect or has expired",
            },
            httpCode: 401,
        },
        login: {
            body: {
                code: 64,
                message: "User logined successfuly",
            },
            httpCode: 201,
        },
        merged: {
            body: {
                code: 63,
                message: "Objects merged successfuly",
            },
            httpCode: 201,
        },
        no_token: {
            body: {
                code: 81,
                message: "No authentication token was provided in the request",
            },
            httpCode: 401,
        },
        not_found: {
            body: {
                code: 50,
                message: "Sorry, that resource does not exist",
            },
            httpCode: 404,
        },
        ok: {
            body: {
                code: 60,
                message: "Ok",
            },
            httpCode: 200,
        },
        permission_denied: {
            body: {
                code: 82,
                message: "You are not authorized to perform this action",
            },
            httpCode: 403,
        },
    },
    production: {
        created: {
            body: {
                code: 61,
                message: "Created successfuly",
            },
            httpCode: 201,
       },
       deleted: {
            body: {
                code: 62,
                message: "Deleted successfuly",
            },
            httpCode: 204,
       },
       edited: {
            body: {
                code: 63,
                message: "Edited successfuly",
            },
            httpCode: 204,
        },
        incorrect_db_error: {
            body: {
                code: 145,
                message: "A database issue appeared:",
            },
            httpCode: 400,
        },
        incorrect_param: {
            body: {
                code: 30,
                message: "",
            },
            httpCode: 400,
        },
        internal_dbs_error: {
            body: {
                code: 21,
                message: "An unknown internal error occurred.",
            },
            httpCode: 500,
        },
        internal_error: {
            body: {
                code: 20,
                message: "An unknown internal error occurred.",
            },
            httpCode: 500,
        },
        invalid_token: {
            body: {
                code: 80,
                message: "The access token used in the request is incorrect or has expired",
            },
            httpCode: 401,
        },
        login: {
            body: {
                code: 64,
                message: "User logined successfuly",
            },
            httpCode: 200,
        },
        no_token: {
            body: {
                code: 81,
                message: "No authentication token was provided in the request",
            },
            httpCode: 401,
        },
        not_found: {
            body: {
                code: 50,
                message: "Sorry, that resource does not exist",
            },
            httpCode: 404,
        },
        ok: {
            body: {
                code: 60,
                message: "Ok",
            },
            httpCode: 200,
       },
        permission_denied: {
            body: {
                code: 82,
                message: "You are not authorized to perform this action",
            },
            httpCode: 403,
        },
    },
};
