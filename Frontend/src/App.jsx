import '@fortawesome/fontawesome-free/css/all.css';
import { Provider } from "react-redux";
import { Navigate, Route, BrowserRouter as Router, Routes } from "react-router-dom";
import "./App.css";
import { AuthProvider } from "./constants/AuthContext";
import store from "./redux/store";

// Components
import CategoryPage from "./components/pages/CategoryPage";
import HomePage from "./components/pages/Homepage";
import LoginPage from "./components/pages/LoginPage";
import ProfilePage from "./components/pages/ProfilePage";
import RegisterPage from "./components/pages/RegisterPage";
import ResetPasswordPage from "./components/pages/ResetPasswordPage";
import Settings from "./components/pages/Setting/Setting";
import SpendingLimitsPage from "./components/pages/SpendingLimitsPage";
import TransactionPage from "./components/pages/TransactionPage";
import WalletPage from "./components/pages/WalletPage";
import PATHS from "./constants/path";
import MainLayout from "./layout/MainLayout";
import PrivateRoute from "./router/PrivateRoute";

function App() {
  return (
    <Provider store={store}>
      <Router>
        <AuthProvider>
          {/* <AppProvider> */}
            <Routes>
              {/* Public Routes */}
              <Route>
                <Route path={PATHS.login} element={<LoginPage />} />
                <Route path={PATHS.register} element={<RegisterPage />} />
                <Route path={PATHS.resetPassword} element={<ResetPasswordPage />} />
              </Route>

              {/* Private Routes */}
              <Route element={<PrivateRoute />}>
                <Route>
                  <Route path={PATHS.homepage} element={<MainLayout />}>
                    <Route index element={<HomePage />} />
                  </Route>
                  <Route path={PATHS.manageCategory} element={<MainLayout />}>
                    <Route index element={<CategoryPage />} />
                  </Route>
                  <Route path={PATHS.manageTransaction} element={<MainLayout />}>
                    <Route index element={<TransactionPage />} />
                  </Route>
                  <Route path={PATHS.manageSpendingLimits} element={<MainLayout />}>
                    <Route index element={<SpendingLimitsPage />} />
                  </Route>
                  <Route path={PATHS.manageWallet} element={<MainLayout />}>
                    <Route index element={<WalletPage />} />
                  </Route>
                  <Route path={PATHS.profile} element={<MainLayout />}>
                    <Route index element={<ProfilePage />} />
                  </Route>
                  <Route path={PATHS.settings} element={<MainLayout />}>
                    <Route index element={<Settings />} />
                  </Route>
                  {/* Redirect any other path to login */}
                  <Route path="*" element={<Navigate to={PATHS.homepage} replace />} />
                </Route>
              </Route>
            </Routes>
          {/* </AppProvider> */}
        </AuthProvider>
      </Router>
    </Provider>
  );
}


export default App;