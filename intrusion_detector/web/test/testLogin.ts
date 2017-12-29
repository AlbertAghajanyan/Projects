import * as chai from 'chai';
import * as request from 'request';
import { db } from './dbConnect';

let token = '';
const url = 'http://localhost:4300/api/v1';

describe('Login', function () {

    it('should get token login', function (done) {
        request.post(url + '/login', {
            form: {
                userName: 'admin@mail.ru',
                password: 'Admin1',
            },
        },
        function (err, res, body){
            chai.expect(res.statusCode).to.equal(200);
            token = JSON.parse(JSON.stringify(res.headers))['set-cookie'][0].slice(13, -8);
            done();
        });
    });

    it('should get error invalid username or password', function (done) {
        request.post(url + '/login', {
            form: {
                userName: 'admin@mail.ru',
                password: 'Admin11',
            },
        },
        function (err, res, body){
            chai.expect(res.statusCode).to.equal(401);
            done();
        });
    });

    it('should get error invalid username or password', function (done) {
        request.post(url + '/login', {
            form: {
                userName: 'admin',
                password: 'Admin1',
            },
        },
        function (err, res, body){
            chai.expect(res.statusCode).to.equal(401);
            done();
        });
    });

    it('should get error no data', function (done) {
        request.post(url + '/login',
        function (err, res, body){
            chai.expect(res.statusCode).to.equal(500);
            done();
        });
    });

    it('should get error password expected', function (done) {
        request.post(url + '/login', {
            form: {
                userName: 'admin@mail.ru',
            },
        },
        function (err, res, body){
            chai.expect(res.statusCode).to.equal(500);
            done();
        });
    });

    it('should get error username expected', function (done) {
        request.post(url + '/login', {
            form: {
                password: 'Admin11',
            },
        },
        function (err, res, body){
            chai.expect(res.statusCode).to.equal(401);
            done();
        });
    });

    after(function(done) {
        db.any('delete from users')
            .then(data => {
                db.any('delete from cameras')
                    .then(newData => {
                        done();
                    })
                    .catch(err => {
                    })
            })
            .catch(err => {
            })
    });
});

