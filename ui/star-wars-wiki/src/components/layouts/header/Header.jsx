import {AppBar, FormControlLabel, FormGroup, styled, Switch, Toolbar, Typography} from "@mui/material";
import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {enableOfflineMode, isOfflineModeEnabled, setOfflineMode} from "../../../store/searchSlice.js";

const CustomAppBar = styled(AppBar)(({ theme }) => ({
  backgroundColor: theme.palette.grey[300],
  color: theme.palette.grey[800],
  boxShadow: "none",
  borderBottom: `2px solid ${theme.palette.divider}`,
  flexGrow: 1
}))

const Header = () => {
  const [offlineToggle, setOfflineToggle] = useState(false);
  const offlineEnabled = useSelector(isOfflineModeEnabled);
  const dispatch = useDispatch();

  const handleOfflineSwitch = () => {
    dispatch(setOfflineMode());
    dispatch(enableOfflineMode(!offlineToggle));
  }

  useEffect(() => {
    setOfflineToggle(offlineEnabled)
  }, [offlineEnabled]);

  return (
    <>
      <CustomAppBar position="sticky">
        <Toolbar>
          <Typography sx={{flexGrow: 1}} variant="h4">Star Wars Wiki</Typography>

          <FormGroup>
            <FormControlLabel control={<Switch checked={offlineToggle} onChange={handleOfflineSwitch}/>} label="Offline Mode"/>
          </FormGroup>
        </Toolbar>
      </CustomAppBar>
    </>
  )
}

export default Header;