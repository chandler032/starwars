import {Box, styled} from "@mui/material";
import Sidebar from "../sidebar/Sidebar.jsx";
import Content from "../content/Content.jsx";
import {useSelector} from "react-redux";
import {getErrorMessage, getShowErrorMessage, getShowResultContent} from "../../../store/searchSlice.js";
import SWAlert from "../../alerts/SWAlert.jsx";

const ParentContainer = styled(Box)(({theme}) => ({
  display: "flex",
  flexDirection: "row",
  height: "100vh",
  backgroundColor: theme.palette.grey[100],
  [theme.breakpoints.down("md")]: {
    flexDirection: "column"
  }
}));

const AppParentContainer = () => {
  const showContentOnRight = useSelector(getShowResultContent);
  const alertErrorMessage = useSelector(getErrorMessage);
  const isError = useSelector(getShowErrorMessage)

  return (
    <ParentContainer>
      <Sidebar/>
      {showContentOnRight ? <Content/> : isError && <SWAlert alertType="error" message={alertErrorMessage}/>}
    </ParentContainer>
  )
}

export default AppParentContainer;