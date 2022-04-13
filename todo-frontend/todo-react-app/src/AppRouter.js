import React from "react";
import "./index.css";
import App from "./App";
import Login from "./Login";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Box from "@material-ui/core/Box";
import Typography from "@material-ui/core/Typography";
import SignUp from "./SignUp";

function Copyright() {
    return (
        <Typography variant="body2" color="textSecondary" align="center">
            {"Copyright © "}
            fsoftwareengineer, {new Date().getFullYear()}
            {"."}
        </Typography>
    );
}

// https://velog.io/@seondal/React-Router-%EC%98%A4%EB%A5%98-6.x.x-%EB%B2%84%EC%A0%84%EC%97%90%EC%84%9C-%EB%B0%94%EB%80%90-%EC%82%AC%EC%9A%A9%EB%B2%95
class AppRouter extends React.Component {
    render() {
        return (
            <Router>
                <div>
                    <Routes>
                        <Route path="/login" element={<Login/>}/>

                        <Route path="/" element={<App/>}/>
                        <Route path="/signup" element={<SignUp />}/>
                    </Routes>
                </div>
                <Box mt={5}>
                    <Copyright/>
                </Box>
            </Router>
        );
    }
}

export default AppRouter;