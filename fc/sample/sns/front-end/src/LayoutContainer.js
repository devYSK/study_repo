
// 노티 컴포넌트 커스텀한것
// import Notification from './NotificationComponent';
import {useEffect, useState} from "react";


import { NotificationContainer, Notification } from "./NotificationComponent";



function LayoutContainer({ children }) {
    const [notificationMessage, setNotificationMessage] = useState('');

    useEffect(() => {

        const eventSource = new EventSource("http://localhost:8080/api/notification/subscribe?token="
            + localStorage.getItem('token'));

        eventSource.addEventListener("alarm", function(event) {
            let data;
            try {
                data = JSON.parse(event.data);
            } catch (e) {
                data = { message: event.data };
            }

            setNotificationMessage(data.message);
            setTimeout(() => setNotificationMessage(''), 10); // 메시지 상태 초기화
        });

        eventSource.onerror = function(error) {
            console.error("EventSource failed:", error);
            eventSource.close();
        };

        return () => {
            eventSource.close();
        };

    }, []);

    return (
        <div>
            {children}
            <NotificationContainer message={notificationMessage} /> {/* NotificationContainer를 사용 */}
        </div>
    );
}

export default LayoutContainer;
