import React, { useEffect, useState } from 'react';

export default function Notification({ message }) {
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
            setSlideIn(false); // 애니메이션을 slideOut으로 변경
            setTimeout(() => setIsVisible(false), 500); // 애니메이션 후에 isVisible 상태 변경
        }, 2000); // 2초 뒤에 실행

        return () => clearTimeout(timer);
    }, []);

    const handleClose = () => {
        setSlideIn(false); // 애니메이션을 slideOut으로 변경
        setTimeout(() => setIsVisible(false), 500); // 애니메이션 후에 isVisible 상태 변경
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
