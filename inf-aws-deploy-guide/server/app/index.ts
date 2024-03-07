import express, {response} from "express"
import dotenv from "dotenv"
dotenv.config()

import * as redis from "redis"
import {createApp} from "./app";

const {PORT, REDIS_URL} = process.env
if (!PORT) new Error("PORT IS required")
if (!REDIS_URL) new Error("PORT IS required")

const startServer = async() => {

    const client = redis.createClient({url: REDIS_URL});
    await client.connect();

    const app = createApp(client)

    app.listen(PORT, () => {
        console.log(`App listening at port ${PORT}`)
    });

}

startServer()