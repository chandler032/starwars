import {Box, styled} from "@mui/material";
import Search from "../../search/Search.jsx";

const SideBarContainer = styled(Box)(({theme}) => ({
  flex: "0 0 25%",
  display: "flex",
  flexDirection: "column",
  backgroundColor: theme.palette.grey[200],
  padding: theme.spacing(2),
  borderRight: `2px solid ${theme.palette.divider}`,
  [theme.breakpoints.down("md")]: {
    flex: "none",
    flexDirection: "row",
    width: "100%",
    position: "sticky",
    top: 0,
    zIndex: 100,
    padding: theme.spacing(1)
  }
}))

const Sidebar = () => {
  return (
    <SideBarContainer>
      <Search/>
    </SideBarContainer>
  )
}

export default Sidebar;