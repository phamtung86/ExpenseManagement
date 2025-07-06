// // contexts/AppContext.js
// import React, { createContext, useContext, useEffect, useState } from 'react';

// const AppContext = createContext();

// export const useApp = () => {
//     const context = useContext(AppContext);
//     if (!context) {
//         throw new Error('useApp must be used within an AppProvider');
//     }
//     return context;
// };

// export const AppProvider = ({ children }) => {
//     const [theme, setTheme] = useState('system');
//     const [language, setLanguage] = useState('vi');
//     const [isInitialized, setIsInitialized] = useState(false);

//     // Initialize app settings from localStorage
//     useEffect(() => {
//         const initializeApp = () => {
//             // Load saved preferences from localStorage
//             const savedTheme = localStorage.getItem('appTheme') || 'system';
//             const savedLanguage = localStorage.getItem('appLanguage') || 'vi';
            
//             setTheme(savedTheme);
//             setLanguage(savedLanguage);
            
//             // Apply theme immediately
//             applyTheme(savedTheme);
            
//             // Set document language
//             document.documentElement.lang = savedLanguage;
            
//             setIsInitialized(true);
//         };

//         initializeApp();
//     }, []);

//     // Apply theme to document
//     const applyTheme = (selectedTheme) => {
//         const root = document.documentElement;
        
//         if (selectedTheme === 'system') {
//             const systemPrefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
//             root.setAttribute('data-theme', systemPrefersDark ? 'dark' : 'light');
//         } else {
//             root.setAttribute('data-theme', selectedTheme);
//         }
//     };

//     // Listen for system theme changes
//     useEffect(() => {
//         if (theme === 'system') {
//             const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
//             const handleChange = () => {
//                 applyTheme('system');
//             };
            
//             mediaQuery.addEventListener('change', handleChange);
//             return () => mediaQuery.removeEventListener('change', handleChange);
//         }
//     }, [theme]);

//     // Change theme
//     const changeTheme = (newTheme) => {
//         setTheme(newTheme);
//         localStorage.setItem('appTheme', newTheme);
//         applyTheme(newTheme);
        
//         // Update userData in localStorage
//         const userData = JSON.parse(localStorage.getItem('userData') || '{}');
//         userData.theme = newTheme;
//         localStorage.setItem('userData', JSON.stringify(userData));
//     };

//     // Change language
//     const changeLanguage = (newLanguage) => {
//         setLanguage(newLanguage);
//         localStorage.setItem('appLanguage', newLanguage);
        
//         // Set document language
//         document.documentElement.lang = newLanguage;
        
//         // Update userData in localStorage
//         const userData = JSON.parse(localStorage.getItem('userData') || '{}');
//         userData.language = newLanguage;
//         localStorage.setItem('userData', JSON.stringify(userData));
        
//         // Dispatch event for components that need to update
//         window.dispatchEvent(new CustomEvent('languageChanged', { 
//             detail: { language: newLanguage } 
//         }));
//     };

//     // Get user settings from localStorage
//     const getUserSettings = () => {
//         const userData = JSON.parse(localStorage.getItem('userData') || '{}');
//         const notifications = JSON.parse(localStorage.getItem('appNotifications') || '{"email": false}');
        
//         return {
//             theme,
//             language,
//             notifications,
//             user: {
//                 id: userData.id || 'user-1',
//                 name: userData.name || 'Người dùng',
//                 email: userData.email || 'user@example.com',
//                 ...userData
//             }
//         };
//     };

//     // Save user settings to localStorage
//     const saveUserSettings = (settings) => {
//         const userData = JSON.parse(localStorage.getItem('userData') || '{}');
//         const updatedUserData = { ...userData, ...settings };
//         localStorage.setItem('userData', JSON.stringify(updatedUserData));
//     };

//     const value = {
//         theme,
//         language,
//         isInitialized,
//         changeTheme,
//         changeLanguage,
//         applyTheme,
//         getUserSettings,
//         saveUserSettings
//     };

//     return (
//         <AppContext.Provider value={value}>
//             {children}
//         </AppContext.Provider>
//     );
// };

// // Language translations helper
// export const translations = {
//     vi: {
//         // Settings page
//         settings: 'Cài đặt',
//         settingsSubtitle: 'Quản lý tùy chọn tài khoản và ứng dụng của bạn',
//         notifications: 'Thông báo',
//         emailNotifications: 'Thông báo Email',
//         emailNotificationsDesc: 'Nhận thông báo qua email',
//         pushNotifications: 'Thông báo đẩy',
//         pushNotificationsDesc: 'Nhận thông báo trên thiết bị',
//         smsNotifications: 'Thông báo SMS',
//         smsNotificationsDesc: 'Nhận thông báo qua tin nhắn',
//         language: 'Ngôn ngữ',
//         languageDesc: 'Thay đổi sẽ có hiệu lực ngay lập tức',
//         theme: 'Giao diện',
//         themeDesc: 'Chọn "Theo hệ thống" để tự động theo cài đặt thiết bị',
//         currentStatus: 'Trạng thái hiện tại:',
//         on: 'Bật',
//         off: 'Tắt',
//         // Theme options
//         light: 'Sáng',
//         dark: 'Tối',
//         system: 'Theo hệ thống',
//         // Success messages
//         languageChanged: 'Ngôn ngữ đã được thay đổi thành công!',
//         themeChanged: 'Giao diện đã được thay đổi thành công!',
//         notificationChanged: 'Cài đặt thông báo đã được cập nhật!'
//     },
//     en: {
//         // Settings page
//         settings: 'Settings',
//         settingsSubtitle: 'Manage your account and app preferences',
//         notifications: 'Notifications',
//         emailNotifications: 'Email Notifications',
//         emailNotificationsDesc: 'Receive notifications via email',
//         pushNotifications: 'Push Notifications',
//         pushNotificationsDesc: 'Receive notifications on device',
//         smsNotifications: 'SMS Notifications',
//         smsNotificationsDesc: 'Receive notifications via SMS',
//         language: 'Language',
//         languageDesc: 'Changes will take effect immediately',
//         theme: 'Theme',
//         themeDesc: 'Choose "System" to automatically follow device settings',
//         currentStatus: 'Current Status:',
//         on: 'On',
//         off: 'Off',
//         // Theme options
//         light: 'Light',
//         dark: 'Dark',
//         system: 'System',
//         // Success messages
//         languageChanged: 'Language changed successfully!',
//         themeChanged: 'Theme changed successfully!',
//         notificationChanged: 'Notification settings updated!'
//     }
// };

// // Translation helper hook
// export const useTranslation = () => {
//     const { language } = useApp();
    
//     const t = (key) => {
//         return translations[language]?.[key] || translations.vi[key] || key;
//     };
    
//     return { t };
// };

// // Helper functions for localStorage management
// export const StorageHelper = {
//     // Get all app settings
//     getAppSettings: () => {
//         return {
//             theme: localStorage.getItem('appTheme') || 'system',
//             language: localStorage.getItem('appLanguage') || 'vi',
//             notifications: JSON.parse(localStorage.getItem('appNotifications') || '{"email": false}'),
//             userData: JSON.parse(localStorage.getItem('userData') || '{}')
//         };
//     },
    
//     // Save app settings
//     saveAppSettings: (settings) => {
//         if (settings.theme) localStorage.setItem('appTheme', settings.theme);
//         if (settings.language) localStorage.setItem('appLanguage', settings.language);
//         if (settings.notifications) localStorage.setItem('appNotifications', JSON.stringify(settings.notifications));
//         if (settings.userData) localStorage.setItem('userData', JSON.stringify(settings.userData));
//     },
    
//     // Clear all app settings
//     clearAppSettings: () => {
//         localStorage.removeItem('appTheme');
//         localStorage.removeItem('appLanguage');
//         localStorage.removeItem('appNotifications');
//         localStorage.removeItem('userData');
//     }
// };