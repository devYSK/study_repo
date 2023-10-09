import React, { useEffect, useState } from 'react';
import { v4 as uuidv4 } from 'uuid';

export function NotificationContainer({ message }) {
    const [notifications, setNotifications] = useState([]);

    const addNotification = (msg) => {
        const id = uuidv4(); // 현재 시간을 ID로 사용 (더 나은 방법은 고유 ID 생성 라이브러리 사용)
        const newNotification = { id, message: msg };
        setNotifications((prev) => {
            if (prev.length >= 10) {
                return [newNotification, ...prev.slice(0, -1)]; // 마지막 알림 제거
            }
            return [newNotification, ...prev];
        });
    };

    const removeNotification = (id) => {
        setNotifications((prev) => prev.filter((notification) => notification.id !== id));
    };

    useEffect(() => {
        if (message) {
            addNotification(message);
        }
    }, [message]);

    return (
        <>
            {notifications.map((notif) => (
                <Notification key={notif.id} id={notif.id} message={notif.message} onClose={removeNotification} />
            ))}
        </>
    );
}



export default function Notification({ id, message, onClose }) {
    const [isVisible, setIsVisible] = useState(true);
    const [slideIn, setSlideIn] = useState(true);

    const notificationSlideKeyframes = `
        @keyframes slideIn {
            from {
                transform: translateX(100%);
            }
            to {
                transform: translateX(0);
            }
        }
        
        @keyframes slideOut {
            from {
                transform: translateX(0);
            }
            to {
                transform: translateX(100%);
            }
        }
    `;

    const notificationStyle = {
        position: 'fixed',
        top: '20px',
        right: '20px',
        padding: '15px 20px',
        backgroundColor: 'white',
        color: '#333',
        borderRadius: '8px',
        boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.1)',
        zIndex: 1000,
        width: '300px',
        overflow: 'hidden',
        border: '1px solid #e0e0e0',
        fontFamily: 'Arial, sans-serif',
        transform: 'translateX(100%)',
        animation: slideIn ? 'slideIn 0.5s forwards' : 'slideOut 0.5s forwards'
    };

    const closeButtonStyle = {
        position: 'absolute',
        right: '15px',
        top: '15px',
        background: 'transparent',
        border: 'none',
        color: '#aaa',
        cursor: 'pointer',
        fontSize: '1rem',
    };

    const titleStyle = {
        fontSize: '1rem',
        fontWeight: 'bold',
        marginBottom: '8px',
    };

    useEffect(() => {
        const timer = setTimeout(() => {
            setSlideIn(false);
            setTimeout(() => {
                onClose(id); // 알림이 사라지면 부모 컴포넌트에 알림 ID 전달
            }, 500);
        }, 2000);

        return () => clearTimeout(timer); // 타이머를 제거하는 클린업 함수 추가
    }, [id, onClose]); // 의존성 배열에 id와 onClose 추가

    const handleClose = () => {
        setSlideIn(false); // 애니메이션을 slideOut으로 변경
        setTimeout(() => onClose(id), 500); // 애니메이션 후에 부모 컴포넌트에 알림 ID 전달
    };

    return (
        isVisible && (
            <>
                <style>{notificationSlideKeyframes}</style>
                <div style={notificationStyle}>
                    <div style={titleStyle}>New Message</div>
                    <button style={closeButtonStyle} onClick={handleClose}>&times;</button>
                    {message}
                </div>
            </>
        )
    );
}