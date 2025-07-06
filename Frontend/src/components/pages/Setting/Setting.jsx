import React, { useEffect, useState } from 'react'
import '../../../assets/Setting.css'
import UserAPI from '../../../services/userService'

export default function Settings() {
    const [notifications, setNotifications] = useState({
        // push: true,
        email: false,
        // sms: true,
    })
    const [language, setLanguage] = useState('vi')
    const [theme, setTheme] = useState('system')
    const [user, setUser] = useState(null);

    const fetchUser = async () => {
        try {
            const response = await UserAPI.getUserById();
            if (response.status === 200) {
                setUser(response.data);
                setNotifications({
                    // push: response.data.noticePush || false,
                    email: response.data.notice || false,
                    // sms: response.data.noticeSms || false,
                });
            }
        } catch (error) {
            console.error("Error fetching user data:", error);
        }
    };

    useEffect(() => {
        fetchUser();
    }, []);

    const changeNotice = async (notice) => {
        if (!user) return;
        try {
            await UserAPI.updateNotice(user.id, notifications.email);
            setUser((prev) => ({ ...prev, notice }));
        } catch (error) {
            console.error("Error updating notice:", error);
        }
    }

    const getLanguageLabel = (lang) => {
        const map = {
            vi: 'Tiếng Việt',
            en: 'English',
            ja: '日本語',
            ko: '한국어',
            zh: '中文',
            fr: 'Français',
            de: 'Deutsch',
            es: 'Español',
        }
        return map[lang] || 'Khác'
    }

    const getThemeLabel = (theme) => {
        return theme === 'light' ? 'Sáng' : theme === 'dark' ? 'Tối' : 'Theo hệ thống'
    }


    return (
        <div className="container">
            <div className="main-title">Cài đặt</div>
            <p className="subtitle">Quản lý tùy chọn tài khoản và ứng dụng của bạn</p>

            <section className="section">
                <div className="section-title">Thông báo</div>

                {[
                    //   { key: 'push', label: 'Thông báo đẩy', desc: 'Nhận thông báo trên thiết bị' },
                    { key: 'email', label: 'Thông báo Email', desc: 'Nhận thông báo qua email' },
                    //   { key: 'sms', label: 'Thông báo SMS', desc: 'Nhận thông báo qua tin nhắn' },
                ].map((item) => (
                    <div className="row" key={item.key}>
                        <div>
                            <div className="label">{item.label}</div>
                            <div className="desc">{item.desc}</div>
                        </div>
                        <label className="switch">
                            <input
                                type="checkbox"
                                checked={user?.notice}
                                onChange={(e) => {
                                    setNotifications((prev) => ({ ...prev, [item.key]: e.target.checked }));
                                    changeNotice();
                                }}
                            />
                            <span className="slider" />
                        </label>
                    </div>
                ))}
            </section>

            <section className="section">
                <div className="section-title">Ngôn ngữ</div>
                <select value={language} onChange={(e) => setLanguage(e.target.value)}>
                    <option value="vi">🇻🇳 Tiếng Việt</option>
                    <option value="en">🇺🇸 English</option>
                    <option value="ja">🇯🇵 日本語</option>
                    <option value="ko">🇰🇷 한국어</option>
                    <option value="zh">🇨🇳 中文</option>
                    <option value="fr">🇫🇷 Français</option>
                    <option value="de">🇩🇪 Deutsch</option>
                    <option value="es">🇪🇸 Español</option>
                </select>
                <p className="desc">Thay đổi sẽ có hiệu lực sau khi khởi động lại ứng dụng</p>
            </section>

            <section className="section">
                <div className="section-title">Giao diện</div>
                <select value={theme} onChange={(e) => setTheme(e.target.value)}>
                    <option value="light">☀️ Sáng</option>
                    <option value="dark">🌙 Tối</option>
                    <option value="system">📱 Theo hệ thống</option>
                </select>
                <p className="desc">Chọn "Theo hệ thống" để tự động theo cài đặt thiết bị</p>
            </section>

            <div className="status-box">
                <strong>Trạng thái hiện tại:</strong><br />
                • Thông báo đẩy: {notifications.push ? 'Bật' : 'Tắt'} <br />
                • Email: {notifications.email ? 'Bật' : 'Tắt'} <br />
                • SMS: {notifications.sms ? 'Bật' : 'Tắt'} <br />
                • Ngôn ngữ: {getLanguageLabel(language)} <br />
                • Theme: {getThemeLabel(theme)}
            </div>
        </div>
    )
}
