/**
 =========================================================
 * Material Dashboard 2 React - v2.1.0
 =========================================================

 * Product Page: https://www.creative-tim.com/product/material-dashboard-react
 * Copyright 2022 Creative Tim (https://www.creative-tim.com)

 Coded by www.creative-tim.com

 =========================================================

 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

import {useState, useEffect, useMemo} from 'react';

// react-router components
import {Routes, Route, Navigate, useLocation} from 'react-router-dom';

// @mui material components
import {ThemeProvider} from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Icon from '@mui/material/Icon';

// Material Dashboard 2 React components
import MDBox from 'components/MDBox';

// Material Dashboard 2 React example components
import Sidenav from 'examples/Sidenav';
import Configurator from 'examples/Configurator';

// Material Dashboard 2 React themes
import theme from 'assets/theme';
import themeRTL from 'assets/theme/theme-rtl';

// Material Dashboard 2 React Dark Mode themes
import themeDark from 'assets/theme-dark';
import themeDarkRTL from 'assets/theme-dark/theme-rtl';

// RTL plugins
import rtlPlugin from 'stylis-plugin-rtl';
import {CacheProvider} from '@emotion/react';
import createCache from '@emotion/cache';

// Material Dashboard 2 React routes
import routes from 'routes';

// Material Dashboard 2 React contexts
import {useMaterialUIController, setMiniSidenav, setOpenConfigurator} from 'context';

// Images
import brandWhite from 'assets/images/logo-ct.png';
import brandDark from 'assets/images/logo-ct-dark.png';

// routes
import ModifyPost from 'layouts/modifypost';
import PostDetail from 'layouts/postdetail';
import LayoutContainer from "./LayoutContainer";


export default function App() {
    const [controller, dispatch] = useMaterialUIController();
    const {
        miniSidenav,
        direction,
        layout,
        openConfigurator,
        sidenavColor,
        transparentSidenav,
        whiteSidenav,
        darkMode,
    } = controller;
    const [onMouseEnter, setOnMouseEnter] = useState(false);
    const [rtlCache, setRtlCache] = useState(null);
    const {pathname} = useLocation();

    // SSE 구독 하고 알림 보내기. 커스텀 노티 때문에 그냥둠
    useEffect(() => {
        // 알림 권한 요청
        if (!("Notification" in window)) {
            console.log("This browser does not support desktop notification");
        } else if (Notification.permission !== "denied") {
            Notification.requestPermission();
        }

        // // SSE 연결 생성
        // const eventSource = new EventSource("http://localhost:8080/api/notification/subscribe?token="
        //     + localStorage.getItem('token'));
        //
        //
        // let notification;
        //
        // eventSource.addEventListener("alarm", function (event) {
        //     console.log("event data : " + event.data);
        //
        //     let data;
        //     try {
        //         // Try to parse data as JSON
        //         data = JSON.parse(event.data);
        //     } catch (e) {
        //         // If not a JSON, use the data as is
        //         data = {message: event.data};
        //     }
        //
        //     // 알림 권한 확인 및 알림 보내기
        //     if (Notification.permission === "granted") {
        //         notification = new Notification("New Message", {
        //             body: data.message,
        //         });
        //     }
        //
        // });
        //
        // eventSource.onerror = function (error) {
        //     console.error("EventSource failed:", error);
        //     eventSource.close();
        // };
        //
        // // 컴포넌트 언마운트 시 연결 종료
        // return () => {
        //     eventSource.close();
        // };

    }, []);

    // Cache for the rtl
    useMemo(() => {
        const cacheRtl = createCache({
            key: 'rtl',
            stylisPlugins: [rtlPlugin],
        });

        setRtlCache(cacheRtl);
    }, []);

    // Open sidenav when mouse enter on mini sidenav
    const handleOnMouseEnter = () => {
        if (miniSidenav && !onMouseEnter) {
            setMiniSidenav(dispatch, false);
            setOnMouseEnter(true);
        }
    };

    // Close sidenav when mouse leave mini sidenav
    const handleOnMouseLeave = () => {
        if (onMouseEnter) {
            setMiniSidenav(dispatch, true);
            setOnMouseEnter(false);
        }
    };

    // Change the openConfigurator state
    const handleConfiguratorOpen = () => setOpenConfigurator(dispatch, !openConfigurator);

    // Setting the dir attribute for the body element
    useEffect(() => {
        document.body.setAttribute('dir', direction);
    }, [direction]);

    // Setting page scroll to 0 when changing the route
    useEffect(() => {
        document.documentElement.scrollTop = 0;
        document.scrollingElement.scrollTop = 0;
    }, [pathname]);

    const getRoutes = (allRoutes) =>
        allRoutes.map((route) => {
            if (route.collapse) {
                return getRoutes(route.collapse);
            }

            if (route.route) {
                return <Route exact path={route.route} element={route.component} key={route.key}/>;
            }

            return null;
        });

    const configsButton = (
        <MDBox
            display="flex"
            justifyContent="center"
            alignItems="center"
            width="3.25rem"
            height="3.25rem"
            bgColor="white"
            shadow="sm"
            borderRadius="50%"
            position="fixed"
            right="2rem"
            bottom="2rem"
            zIndex={99}
            color="dark"
            sx={{cursor: 'pointer'}}
            onClick={handleConfiguratorOpen}
        >
            <Icon fontSize="small" color="inherit">
                settings
            </Icon>
        </MDBox>
    );

    return direction === 'rtl' ? (
        <LayoutContainer>
            <CacheProvider value={rtlCache}>
                <ThemeProvider theme={darkMode ? themeDarkRTL : themeRTL}>
                    <CssBaseline/>
                    {layout === 'dashboard' && (
                        <>
                            <Sidenav
                                color={sidenavColor}
                                brand={(transparentSidenav && !darkMode) || whiteSidenav ? brandDark : brandWhite}
                                brandName="Simple SNS"
                                routes={routes}
                                onMouseEnter={handleOnMouseEnter}
                                onMouseLeave={handleOnMouseLeave}
                            />
                            <Configurator/>
                        </>
                    )}
                    {layout === 'vr' && <Configurator/>}
                    <Routes>
                        {getRoutes(routes)}
                        <Route path="*" element={<Navigate to="/feed"/>}/>
                    </Routes>
                </ThemeProvider>
            </CacheProvider>
        </LayoutContainer>
    ) : (
        <LayoutContainer>
            <ThemeProvider theme={darkMode ? themeDark : theme}>
                <CssBaseline/>
                {layout === 'dashboard' && (
                    <>
                        <Sidenav
                            color={sidenavColor}
                            brand={(transparentSidenav && !darkMode) || whiteSidenav ? brandDark : brandWhite}
                            brandName="Simple SNS"
                            routes={routes}
                            onMouseEnter={handleOnMouseEnter}
                            onMouseLeave={handleOnMouseLeave}
                        />
                        <Configurator/>
                    </>
                )}
                {layout === 'vr' && <Configurator/>}
                <Routes>
                    {getRoutes(routes)}
                    <Route path="*" element={<Navigate to="/feed"/>}/>
                    <Route path="/modify-post" element=<ModifyPost/> />
                    <Route path="/post-detail" element=<PostDetail/> />
                </Routes>
            </ThemeProvider>
        </LayoutContainer>
    );
}