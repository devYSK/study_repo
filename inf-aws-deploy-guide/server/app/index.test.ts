import request from "supertest"
import {describe} from "node:test";
import {createApp, LIST_KEY, RedisClient} from "./app"
import * as redis from "redis";

let app: Express.Application
let client:RedisClient

const REDIS_URL = "redis://default:test_env@localhost:6380"; // 실행시키고 해야함 로컬에서

beforeAll(async () => {
    client = redis.createClient({url: REDIS_URL});
    await client.connect();
    app = createApp(client);
})

beforeEach(async () => {
    await client.flushDb();
})

afterAll(async () => {
    await client.flushDb();
    await client.quit();
})

describe("POST /messages", () => {
    it("response with a success message", async () => {
        // @ts-ignore
        const response = await request(app).post("/messages").send({
            message: "Testing with redis"
        });

        expect(response.status).toBe(200)
        expect(response.text).toBe("Message added to list")
    });
});

describe("GET /messages", () => {
    it("respons with all messages", async () => {
        await client.lPush(LIST_KEY, ["msg1", "msg2"])
        const response = await request(app as any).get("/messages")

        expect(response.status).toBe(200)
        expect(response.body).toEqual(["msg2", "msg1"]);

    })
})